package com.apitechnosoft.ipad.filepicker;

import android.content.Context;
import android.net.Uri;

import com.apitechnosoft.ipad.exception.FNExceptionUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author AST Inc.
 */
public class PicassoTrustAll {

	private static Picasso _instance;

	public static Picasso instance(Context context) {
		if (_instance == null) {
			_instance = imageLoader(context);
		}
		return _instance;
	}

	private static Picasso imageLoader(Context context) {
		Picasso.Builder builder = new Picasso.Builder(context);
		builder.downloader(new UrlConnectionDownloader(context) {
			@Override
			protected HttpURLConnection openConnection(Uri path) throws IOException {
				HttpsURLConnection connection = (HttpsURLConnection) super.openConnection(path);
				try {
					connection.setSSLSocketFactory(new FNTLSSocketFactory());
				} catch (KeyManagementException | NoSuchAlgorithmException e) {
					FNExceptionUtil.logException(e);
				}
				return connection;
			}
		});
		return builder.build();
	}

}
