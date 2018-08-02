package com.apitechnosoft.ipad.manager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class DeviceManager {

	/**
	 * openSettings
	 * redirecting to app settings
	 * @param activityContext
	 * @param requestCode
	 */
	public static void openSettings(Activity activityContext, int requestCode)
	{
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", activityContext.getPackageName(), null);
		intent.setData(uri);
		activityContext.startActivityForResult(intent, requestCode);
	}
}
