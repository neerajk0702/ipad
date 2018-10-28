package com.apitechnosoft.ipad.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.TermsConditionActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.google.gson.Gson;


public class PressFragment extends MainFragment {
    TextView nov, oct, dec, oct2;
    HeaderFragment headerFragment;
    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_press;
    }
    @Override
    protected void loadView() {
        nov = findViewById(R.id.nov);
        oct = findViewById(R.id.oct);
        dec = findViewById(R.id.dec);
        oct2 = findViewById(R.id.oct2);
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
        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(1);
            }
        });
        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(2);
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(3);
            }
        });
        oct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opneDetail(4);
            }
        });
    }

    private void opneDetail(int key) {
        Bundle bundle = new Bundle();
        bundle.putString("headerTxt", "Press Detail");
        bundle.putInt("Key",key);
        ApplicationHelper.application().getActivity().updateFragment(new PressDetailFragment(), bundle);
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
