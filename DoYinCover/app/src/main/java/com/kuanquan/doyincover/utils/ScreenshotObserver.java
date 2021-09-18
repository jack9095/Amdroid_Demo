package com.kuanquan.doyincover.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class ScreenshotObserver extends ContentObserver {
    private static final String TAG = "ScreenshotObserver";
    private static final String EXTERNAL_CONTENT_URI_MATCHER =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();

    private static final String[] PROJECTION = new String[]{
            MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED
    };
    private static final String SORT_ORDER = MediaStore.Images.Media.DATE_ADDED + " DESC";
    private static final long DEFAULT_DETECT_WINDOW_SECONDS = 10;

    public interface OnScreenshotListener {
        void onScreenshot(String path);
    }

    private OnScreenshotListener mListener;
    private Context mContext;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ScreenshotObserver(@Nullable Handler handler, @NonNull Context context) {
        super(handler);
        this.mContext = context.getApplicationContext();
    }

    /**
     * Screen event subscription
     *
     * @param listener
     */
    //@RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
    public void subscript(@NonNull OnScreenshotListener listener) {
        this.mListener = listener;
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, this);

    }

    /**
     * UnSubscribe screen screenshot event
     */
    public void unSubscript() {
        this.mListener = null;
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Log.i("ScreenshotObserver", "onChange: " + selfChange + ", " + uri.toString());
        if (uri.toString().startsWith(EXTERNAL_CONTENT_URI_MATCHER)) {
            Cursor cursor = null;
            try {

                cursor = mContext.getContentResolver().query(uri, PROJECTION, null, null, SORT_ORDER);
                if (cursor != null && cursor.moveToFirst()) {
                    String path = cursor.getString(
                            cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    long dateAdded = cursor.getLong(cursor.getColumnIndex(
                            MediaStore.Images.Media.DATE_ADDED));
                    long currentTime = System.currentTimeMillis() / 1000;
                    Log.i("ScreenshotObserver", "path: " + path + ", dateAdded: " + dateAdded +
                            ", currentTime: " + currentTime);
                    if (matchPath(path) && matchTime(currentTime, dateAdded)) {
                        if (mListener != null) {
                            mListener.onScreenshot(path);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ScreenshotObserver", "open cursor fail exception =" + e.toString());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        super.onChange(selfChange, uri);
    }

    private boolean matchPath(String path) {
        return path.toLowerCase().contains("screenshots")
                || path.toLowerCase().contains("screenshot")
                || path.contains("截屏")
                || path.contains("截图");
    }

    private boolean matchTime(long currentTime, long dateAdded) {
        return Math.abs(currentTime - dateAdded) <= DEFAULT_DETECT_WINDOW_SECONDS;
    }
}