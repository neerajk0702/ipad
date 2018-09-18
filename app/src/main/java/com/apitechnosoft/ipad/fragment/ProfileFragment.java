package com.apitechnosoft.ipad.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.ChnagePassword;
import com.apitechnosoft.ipad.activity.LoginActivity;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.google.gson.Gson;

import static com.apitechnosoft.ipad.utils.ASTUIUtil.showToast;

public class ProfileFragment extends MainFragment {

    CardView emailviewLayout;
    private ImageView profileImg;
    private TextView loginUserName, userprofile, userMailAdd;
    TextView lname, fname;
    private Button submitButton;
    private View changePasswordLayout;
    String fnamee, lnamee, emailStr;

    @Override
    protected int fragmentLayout() {
        return R.layout.profile;
    }

    @Override
    protected void loadView() {
        this.profileImg = this.findViewById(R.id.profileImg);
        this.loginUserName = this.findViewById(R.id.loginUserName);
        this.submitButton = this.findViewById(R.id.submitButton);
        this.changePasswordLayout = this.findViewById(R.id.changePasswordLayout);
        this.emailviewLayout = this.findViewById(R.id.emailviewLayout);
        this.findViewById(R.id.cancelButton).setVisibility(View.GONE);
    }

    @Override
    protected void setClickListeners() {
        this.profileImg.setOnClickListener(this);
        this.submitButton.setOnClickListener(this);
        this.changePasswordLayout.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {

    }


    @Override
    public void dataToView() {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.changePasswordLayout) {
            Intent intent = new Intent(getContext(), ChnagePassword.class);
            startActivity(intent);
        } else if (view.getId() == R.id.submitButton) {
            fnamee = getTextFromView(fname);
            lnamee = getTextFromView(lname);
            SharedPreferences prefs = getContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            if (prefs != null) {
                emailStr = prefs.getString("UserId", "");
            }
            ChnageName();
        }
    }

    private void ChnageName() {
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            final String url = Contants.BASE_URL + Contants.UpdateUserName + "username=" + emailStr + "&" + "fname=" + fnamee + "&" + "lname=" + lnamee;
            serviceCaller.CallCommanServiceMethod(url, "Login", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.setUserId(getContext(), emailStr);
                                Toast.makeText(getContext(), "Login Successfully.", Toast.LENGTH_LONG).show();
                                Intent intentLoggedIn = new Intent(getContext(), MainActivity.class);
                                startActivity(intentLoggedIn);
                            } else {
                                Toast.makeText(getContext(), "Login not Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Login not Successfully!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (dotDialog.isShowing()) {
                        dotDialog.dismiss();
                    }
                }
            });
        } else {
            showToast(Contants.OFFLINE_MESSAGE);
        }

    }
}
