package com.apitechnosoft.ipad.filepicker.camera;

import android.content.Context;
import android.content.Intent;

import com.apitechnosoft.ipad.filepicker.FNFilePickerConfig;


/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public interface FNCameraModule {

	Intent getCameraIntent(Context context, FNFilePickerConfig config);

	void getImage(Context context, Intent intent, OnImageReadyListener imageReadyListener);
}
