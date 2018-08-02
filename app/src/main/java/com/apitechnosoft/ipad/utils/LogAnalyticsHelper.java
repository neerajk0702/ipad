package com.apitechnosoft.ipad.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Neeraj on 18-june-18.
 */
public class LogAnalyticsHelper {
    private FirebaseAnalytics mFirebaseAnalytics;
    private Context context;

    public LogAnalyticsHelper(Context context) {
        this.context = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logEvent(String eventName, Bundle params) {
        if (params != null) {
//            params.putString("image_name", name);
//            params.putString("full_text", text);
            mFirebaseAnalytics.logEvent(eventName, params);
        }
    }

    public void logSetUserProperty(String userPropertyName, String userPropertyValue) {
        mFirebaseAnalytics.setUserProperty(userPropertyName,userPropertyValue);
    }

}
