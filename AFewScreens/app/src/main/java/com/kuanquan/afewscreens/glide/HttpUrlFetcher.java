package com.kuanquan.afewscreens.glide;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Synthetic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpUrlFetcher implements DataFetcher<InputStream>, Runnable {
    private static final String TAG = HttpUrlFetcher.class.getSimpleName();
    private static final int MAXIMUM_REDIRECTS = 5;
    @VisibleForTesting
    static final HttpUrlFetcher.HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY =
            new HttpUrlFetcher.DefaultHttpUrlConnectionFactory();
    /**
     * Returned when a connection error prevented us from receiving an http error.
     */
    private static final int INVALID_STATUS_CODE = -1;

    private final GlideUrl glideUrl;
    private final int timeout;
    private final HttpUrlFetcher.HttpUrlConnectionFactory connectionFactory;
    private volatile boolean isCancelled;
    private DataCallback<? super InputStream> callback;
    private static ThreadPoolExecutor sDownloadExecutor;

    static {
        int cpuCounts = Runtime.getRuntime().availableProcessors();
        sDownloadExecutor = new ThreadPoolExecutor(Math.max(2, Math.min(cpuCounts - 1, 4)),
                cpuCounts * 2 + 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
    }


    public HttpUrlFetcher(GlideUrl glideUrl, int timeout) {
        this(glideUrl, timeout, DEFAULT_CONNECTION_FACTORY);
    }

    @VisibleForTesting
    HttpUrlFetcher(GlideUrl glideUrl, int timeout, HttpUrlFetcher.HttpUrlConnectionFactory connectionFactory) {
        this.glideUrl = glideUrl;
        this.timeout = timeout;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void loadData(@NonNull Priority priority,
                         @NonNull DataCallback<? super InputStream> callback) {
        this.callback = callback;
        sDownloadExecutor.execute(this);
    }

    private InputStream loadDataWithRedirects(URL url, int redirects, URL lastUrl,
                                              Map<String, String> headers) throws IOException {
        if (redirects >= MAXIMUM_REDIRECTS) {
            throw new HttpException("Too many (> " + MAXIMUM_REDIRECTS + ") redirects!");
        } else {
            // Comparing the URLs using .equals performs additional network I/O and is generally broken.
            try {
                if (lastUrl != null && url.toURI().equals(lastUrl.toURI())) {
                    throw new HttpException("In re-direct loop");

                }
            } catch (URISyntaxException e) {
                // Do nothing, this is best effort.
            }
        }

        HttpURLConnection urlConnection = connectionFactory.build(url);
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            urlConnection.addRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }
        urlConnection.setConnectTimeout(timeout);
        urlConnection.setReadTimeout(timeout);
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);

        // Stop the urlConnection instance of HttpUrlConnection from following redirects so that
        // redirects will be handled by recursive calls to this method, loadDataWithRedirects.
        urlConnection.setInstanceFollowRedirects(false);

        long startTime = System.currentTimeMillis();
        // Connect explicitly to avoid errors in decoders if connection fails.
        urlConnection.connect();
        // Set the stream so that it's closed in cleanup to avoid resource leaks. See #2352.
        if (isCancelled) {
            return null;
        }
        final int statusCode = urlConnection.getResponseCode();
        try {
            if (isHttpOk(statusCode)) {
                return getStreamForSuccessfulRequest(urlConnection, startTime);
            } else if (isHttpRedirect(statusCode)) {
                String redirectUrlString = urlConnection.getHeaderField("Location");
                if (TextUtils.isEmpty(redirectUrlString)) {
                    throw new HttpException("Received empty or null redirect url");
                }
                URL redirectUrl = new URL(url, redirectUrlString);
                return loadDataWithRedirects(redirectUrl, redirects + 1, url, headers);
            } else if (statusCode == INVALID_STATUS_CODE) {
                throw new HttpException(statusCode);
            } else {
                throw new HttpException(urlConnection.getResponseMessage(), statusCode);
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // Referencing constants is less clear than a simple static method.
    private static boolean isHttpOk(int statusCode) {
        return statusCode / 100 == 2;
    }

    // Referencing constants is less clear than a simple static method.
    private static boolean isHttpRedirect(int statusCode) {
        return statusCode / 100 == 3;
    }

    private InputStream getStreamForSuccessfulRequest(HttpURLConnection urlConnection, long startTime)
            throws IOException {
        InputStream stream;
        if (TextUtils.isEmpty(urlConnection.getContentEncoding())) {
            int contentLength = urlConnection.getContentLength();
            stream = ContentLengthInputStream.obtain(urlConnection.getInputStream(), contentLength);
        } else {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Got non empty content encoding: " + urlConnection.getContentEncoding());
            }
            stream = urlConnection.getInputStream();
        }
        int contentLength = urlConnection.getContentLength();
        if (contentLength <= 0) {
            stream.close();
            return null;
        }
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(contentLength);
            byte[] buf = new byte[1024];
            int length;
            while (!isCancelled && (length = stream.read(buf)) > 0) {
                byteBuffer.put(buf, 0, length);
            }
            stream.close();
            if (isCancelled) {
                return null;
            }
            stream = new ByteArrayInputStream(byteBuffer.array(), 0, contentLength);
            return stream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void cleanup() {
    }

    @Override
    public void cancel() {
        isCancelled = true;
        sDownloadExecutor.remove(this);
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

    @Override
    public void run() {
        long startTime = LogTime.getLogTime();
        long startTimeNew = System.currentTimeMillis();
        try {
            InputStream result = loadDataWithRedirects(glideUrl.toURL(), 0, null, glideUrl.getHeaders());
            if (result != null) {
                callback.onDataReady(result);
            } else {
                callback.onLoadFailed(new Exception("Canceled"));
            }
        } catch (Exception e) {
            callback.onLoadFailed(e);
        } finally {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Finished http url fetcher fetch in " + LogTime.getElapsedMillis(startTime));
            }
        }
    }

    interface HttpUrlConnectionFactory {
        HttpURLConnection build(URL url) throws IOException;
    }

    private static class DefaultHttpUrlConnectionFactory implements HttpUrlFetcher.HttpUrlConnectionFactory {

        @Synthetic
        DefaultHttpUrlConnectionFactory() {
        }

        @Override
        public HttpURLConnection build(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }
    }
}

