package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.adapter.NotificationAdapter;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentData;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Notificationlist;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FZProgressBar;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String UserId;
    RecyclerView notificationrecycler_view;
    NotificationAdapter adapter;
    ArrayList<Notificationlist> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        if (ASTUtil.isTablet(NotificationActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {

        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
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
        Button clearall = findViewById(R.id.clearall);

        notificationrecycler_view = findViewById(R.id.notificationrecycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationrecycler_view.setLayoutManager(linearLayoutManager);
        getAllNotification();

        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRemoveAllNotification();
            }
        });

    }

    private void getAllNotification() {
        if (ASTUIUtil.isOnline(NotificationActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(NotificationActivity.this);
            dotDialog.show();

            ServiceCaller serviceCaller = new ServiceCaller(NotificationActivity.this);
            final String url = Contants.BASE_URL + Contants.Getallnotification + "username=" + UserId;
            serviceCaller.CallCommanServiceMethod(url, "getAllNotification", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            Log.d(Contants.LOG_TAG, "Get All Notification**" + result);
                            arrayList = new ArrayList();
                            if (data.getNotificationlist() != null && data.getNotificationlist().length > 0) {
                                for (Notificationlist notification : data.getNotificationlist()) {
                                    arrayList.add(notification);
                                }
                                adapter = new NotificationAdapter(NotificationActivity.this, arrayList);
                                notificationrecycler_view.setAdapter(adapter);
                            }

                        } else {
                            Toast.makeText(NotificationActivity.this, "Notification not found!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ASTUIUtil.showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }

                }
            });
        } else {
            ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
        }
    }

    private void getRemoveAllNotification() {
        if (ASTUIUtil.isOnline(NotificationActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(NotificationActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(NotificationActivity.this);
            final String url = Contants.BASE_URL + Contants.DeleteAllNotification + "username=" + UserId + "&" + "emailid=" + UserId + "&" + "sno=" + 0;
            serviceCaller.CallCommanServiceMethod(url, "getRemoveAllNotification", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            Log.d(Contants.LOG_TAG, "Get All RemoveAllNotification**" + result);
                            if (data.getAllnotificationstatus().equals("true")) {
                                if (arrayList != null) {
                                    finish();
                                }
                            }

                        }
                    } else {
                        ASTUIUtil.showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            ASTUIUtil.showToast(Contants.OFFLINE_MESSAGE);
        }
    }
}
