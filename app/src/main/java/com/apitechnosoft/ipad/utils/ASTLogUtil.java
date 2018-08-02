package com.apitechnosoft.ipad.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.apitechnosoft.ipad.ApplicationHelper;


/**
 * @author AST Inc.
 */
public class ASTLogUtil {
	public static void sendToLogger(String logs) {
		sendToLogger(ApplicationHelper.application().getActivity(), logs);
	}

	public static void sendToLogger(Context context, String logs) {
		try {

			Intent sendIntent = new Intent();
			sendIntent.setAction("SEND_LOGS_TO_LOGGER");
			sendIntent.putExtra("LOG_DATA", logs);
			context.sendBroadcast(sendIntent);
		} catch (Exception e) {

		}
	}
}
