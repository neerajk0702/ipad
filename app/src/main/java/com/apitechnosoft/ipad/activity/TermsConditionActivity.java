package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.fragment.HeaderFragment;
import com.apitechnosoft.ipad.fragment.MainFragment;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.google.gson.Gson;

public class TermsConditionActivity extends MainFragment {
    @Override
    protected int fragmentLayout() {
        return R.layout.activity_terms_condition;
    }
    HeaderFragment headerFragment;
    @Override
    protected void loadView() {

    }

    @Override
    protected void setClickListeners() {
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {
        getAllNotification();
    }
    public void getAllNotification() {
        try {
            String UserId = "";
            SharedPreferences prefs = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            if (prefs != null) {
                UserId = prefs.getString("UserId", "");
            }
            if (ASTUIUtil.isOnline(getContext())) {
                final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
                // dotDialog.show();

                ServiceCaller serviceCaller = new ServiceCaller(getContext());
                final String url = Contants.BASE_URL + Contants.Getallnotification + "username=" + UserId;
                serviceCaller.CallCommanServiceMethod(url, "getAllNotification", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                            if (data != null) {
                                loadcartdata(data.getNotificationcount() + "");
                            }
                        }
                    }
                });
            }
        } catch (Exception E) {

        }
    }
    protected void loadcartdata(String count) {
        if (getHostActivity() == null) {
            return;
        }
        this.headerFragment = this.getHostActivity().headerFragment();
        if (headerFragment == null) {
            return;
        }
        this.headerFragment.setVisiVilityNotificationIcon(true);
        this.headerFragment.updateNotification(count);
    }
}

