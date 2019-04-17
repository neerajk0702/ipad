package com.apitechnosoft.ipad.framework;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;


import com.apitechnosoft.ipad.constants.Contants;

import org.apache.http.conn.ssl.AbstractVerifier;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.CertificatePinner;
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
            //b.certificatePinner(certificatePinner);
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


    /*private const val DOMAIN = "*.stylingandroid.com"
    private const val PIN = "sha256/htJkaSJB+j8Ckv7ovGieQJYqyV/M4K7YRt4je18A7T4="
    private const val BACKUP = "sha256/x9SZw6TwIqfmvrLZ/kz1o0Ossjmn728BnBKpUFqGNVM="

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor(Logger { println(it) }).setLevel(Level.BASIC))
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                certificatePinner(CertificatePinner.Builder()
                        .add(DOMAIN, PIN)
                        .add(DOMAIN, BACKUP)
                        .build()
                )
            }
        }.build()
    }*/
}
