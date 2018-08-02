package com.apitechnosoft.ipad.filepicker.camera;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;


/**
 * Created 05-06-2017
 *
 * @author AST Inc.
 */
public class FNCameraHelper {

	public static boolean checkCameraAvailability(Context context) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		boolean isAvailable = intent.resolveActivity(context.getPackageManager()) != null;

		if (!isAvailable) {
			Context appContext = context.getApplicationContext();
			Toast.makeText(appContext,
					appContext.getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
		}
		return isAvailable;
	}
}
