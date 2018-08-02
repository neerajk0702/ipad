package com.apitechnosoft.ipad;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * <h4>Created</h4> 04/07/16
 *
 * @author AST Inc.
 */
public class ActivityLifecycleHandler implements Application.ActivityLifecycleCallbacks{
    public boolean isINForground(){
        return isInForGround;
    }
    boolean isInForGround;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }


    @Override
    public void onActivityResumed(Activity activity) {
        isInForGround=true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isInForGround=false;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
