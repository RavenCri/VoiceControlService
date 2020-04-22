package com.util;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: raven
 * @Date: 2019/11/14 16:12
 * @Description:
 */
public class HttpUtil {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String sendPostRequest(String url,String data){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build();

        RequestBody requestBody = RequestBody.create(JSON,data);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        try {
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            return res;
        } catch (IOException e) {
            System.out.println("超时");
            return null;
           // e.printStackTrace();
        }
        //return null;
    }
}