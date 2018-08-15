package com.apitechnosoft.ipad.framework;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.apitechnosoft.ipad.constants.Contants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Neeraj on 3/22/2017.
 */

public abstract class FileUploaderHelper extends AsyncTask<String, Integer, String> implements UploadLicenseImageCallback {

    Context mContext;
    HashMap<String, String> payload;
    MultipartBody.Builder multipartBody;
    String url;

    public FileUploaderHelper(Context context, HashMap<String, String> payload, MultipartBody.Builder multipartBody, String url) {
        this.mContext = context;
        this.payload = payload;
        this.multipartBody = multipartBody;
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        //String sourceImageFile = params[0];
        String responce = uploadImage(payload);
        return responce;
    }

    @Override
    protected void onPostExecute(String result) {
        if (Contants.IS_DEBUG_LOG) {
            Log.d(Contants.LOG_TAG, "image uploaded successfully****" + result);
        }
        receiveData(result);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    public String uploadImage(HashMap<String, String> mapList) {
        try {

            for (Map.Entry<String, String> entry : mapList.entrySet()) {
                if (entry.getValue() != null) {
                    multipartBody.addFormDataPart(entry.getKey(), entry.getValue());//String.valueOf(entry.getValue()) for int value
                    Log.d(Contants.LOG_TAG, String.format("%s -> %s%n", entry.getKey(), entry.getValue()));
                }
            }
            RequestBody requestBody = multipartBody.build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient.Builder b = new OkHttpClient.Builder();
            b.connectTimeout(180, TimeUnit.SECONDS);
            b.writeTimeout(180, TimeUnit.SECONDS);
            b.readTimeout(180, TimeUnit.SECONDS);
            OkHttpClient client = b.build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            Log.e(Contants.LOG_TAG, "Error: " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public abstract void receiveData(String result);


}
