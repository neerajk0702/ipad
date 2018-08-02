package com.apitechnosoft.ipad.filepicker.camera;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;


import com.apitechnosoft.ipad.filepicker.FNFilePickerConfig;
import com.apitechnosoft.ipad.filepicker.helper.ASTImageUtil;
import com.apitechnosoft.ipad.filepicker.model.PhotoFactory;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class DefaultCameraModule implements FNCameraModule, Serializable {

	protected String currentImagePath;

	public Intent getCameraIntent(Context context) {
		return getCameraIntent(context, new FNFilePickerConfig(context));
	}

	@Override
	public Intent getCameraIntent(Context context, FNFilePickerConfig config) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File imageFile = ASTImageUtil.createImageFile(config.getImageDirectory());
		if (imageFile != null) {
			Context appContext = context.getApplicationContext();
			String providerName = String.format(Locale.ENGLISH, "%s%s", appContext.getPackageName(), ".imagepicker.provider");
			Uri uri = FileProvider.getUriForFile(appContext, providerName, imageFile);
			currentImagePath = "file:" + imageFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			ASTImageUtil.grantAppPermission(context, intent, uri);
			return intent;
		}
		return null;
	}

	@Override
	public void getImage(final Context context, Intent intent, final OnImageReadyListener imageReadyListener) {
		if (imageReadyListener == null) {
			throw new IllegalStateException("OnImageReadyListener must not be null");
		}
		if (currentImagePath == null) {
			imageReadyListener.onImageReady(null);
			return;
		}

		final Uri imageUri = Uri.parse(currentImagePath);
		if (imageUri != null) {
			MediaScannerConnection.scanFile(context.getApplicationContext(),
					new String[] { imageUri.getPath() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(String path, Uri uri) {
							Log.v("ImagePicker", "File " + path + " was scanned successfully: " + uri);

							if (path == null) {
								path = currentImagePath;
							}
							imageReadyListener.onImageReady(PhotoFactory.singleListFromPath(path));
							ASTImageUtil.revokeAppPermission(context, imageUri);
						}
					});
		}
	}
}
