package com.apitechnosoft.ipad.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.google.gson.Gson;

public class PressDetailFragment extends MainFragment {
    TextView detail;
    int detailvalue;
    HeaderFragment headerFragment;
    @Override
    protected int fragmentLayout() {
        return R.layout.press_detail_fragment;
    }

    @Override
    protected void loadView() {
        detail = findViewById(R.id.detail);
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
        if(detailvalue==1) {
            detail.setText(getString(R.string.novdetail));
        }else if(detailvalue==2){
           detail.setText(getString(R.string.octdetail));
        }else if(detailvalue==3){
            detail.setText(getString(R.string.decdetail));
        }else if(detailvalue==4){
           detail.setText(getString(R.string.oct2detail));
        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        detailvalue = getArguments().getInt("Key", 0);
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

