package com.apitechnosoft.ipad.filepicker.camera;

import android.content.Context;
import android.content.Intent;

import com.apitechnosoft.ipad.filepicker.model.PhotoFactory;


/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class ImmediateCameraModule extends DefaultCameraModule {

	@Override
	public void getImage(Context context, Intent intent, OnImageReadyListener imageReadyListener) {
		if (imageReadyListener == null) {
			throw new IllegalStateException("OnImageReadyListener must not be null");
		}
		if (currentImagePath == null) {
			imageReadyListener.onImageReady(null);
		}
		imageReadyListener.onImageReady(PhotoFactory.singleListFromPath(currentImagePath));
	}
}
