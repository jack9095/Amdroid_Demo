package com.kuanquan.uploadfile;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class UploadToServer {


    public static String postFile(String url,Map<String, Object> param, File file) throws ClientProtocolException, IOException {
        String res = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(getMutipartEntry(param,file));
        CloseableHttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            res = EntityUtils.toString(entity, "UTF-8");
            response.close();
        } else {
            res = EntityUtils.toString(entity, "UTF-8");
            response.close();
            throw new IllegalArgumentException(res);
        }
        return res;
    }

    private static MultipartEntity getMutipartEntry(Map<String, Object> param, File file) throws UnsupportedEncodingException {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空");
        }
        FileBody fileBody = new FileBody(file);
        FormBodyPart filePart = new FormBodyPart("file", fileBody);
        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart(filePart);

        Iterator<String> iterator = param.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            FormBodyPart field = new FormBodyPart(key, new StringBody((String) param.get(key)));
            multipartEntity.addPart(field);

        }
        return multipartEntity;
    }

}