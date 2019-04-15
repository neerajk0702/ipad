package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.fragment.HeaderFragment;
import com.apitechnosoft.ipad.fragment.MainFragment;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;

public class PrivacyActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        if (ASTUtil.isTablet(PrivacyActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataToView();
    }

    protected void dataToView() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView back = toolbar.findViewById(R.id.back);
        back.setTypeface(materialdesignicons_font);
        back.setText(Html.fromHtml("&#xf30d;"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAllNotification();
    }

    public void getAllNotification() {
        try {
            String UserId = "";
            SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            if (prefs != null) {
                UserId = prefs.getString("UserId", "");
            }
            if (ASTUIUtil.isOnline(PrivacyActivity.this)) {
                final ASTProgressBar dotDialog = new ASTProgressBar(PrivacyActivity.this);
                // dotDialog.show();

                ServiceCaller serviceCaller = new ServiceCaller(PrivacyActivity.this);
                final String url = Contants.BASE_URL + Contants.Getallnotification + "username=" + UserId;
                serviceCaller.CallCommanServiceMethod(url, "getAllNotification", new IAsyncWorkCompletedCallback() {
                    @Override
                    public void onDone(String result, boolean isComplete) {
                        if (isComplete) {
                            ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                            if (data != null) {
                                //loadcartdata(data.getNotificationcount() + "");
                            }
                        }
                    }
                });
            }
        } catch (Exception E) {

        }
    }

}

