package com.apitechnosoft.ipad.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChnagePassword extends AppCompatActivity {
    Button btnLogIn;
    Typeface materialdesignicons_font;
    EditText edt_phone, oldpwdedit, new_password, new_confirmpassword;
    String passwordStr, emailStr, password1;
    TextView welcom, forgotPasssword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);
        if (ASTUtil.isTablet(ChnagePassword.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        loadView();
        datatoView();
    }

    //get view ids
    public void loadView() {
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        oldpwdedit = (EditText) findViewById(R.id.oldpwdedit);
        new_password = (EditText) findViewById(R.id.new_password);
        new_confirmpassword = (EditText) findViewById(R.id.new_confirmpassword);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");

        TextView password_icon1 = (TextView) findViewById(R.id.password_icon1);
        password_icon1.setTypeface(materialdesignicons_font);
        password_icon1.setText(Html.fromHtml("&#xf33e;"));
        TextView password_icon = (TextView) findViewById(R.id.password_icon);
        password_icon.setTypeface(materialdesignicons_font);
        password_icon.setText(Html.fromHtml("&#xf33e;"));

        TextView password_iconold = (TextView) findViewById(R.id.password_iconold);
        password_iconold.setTypeface(materialdesignicons_font);
        password_iconold.setText(Html.fromHtml("&#xf33e;"));
        welcom = findViewById(R.id.welcom);
        welcom.setText(Html.fromHtml("Welcome to iPad<sup>TM</sup>"));
        forgotPasssword = findViewById(R.id.forgotPasssword);
    }

    //get data from UI
    public void datatoView() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    callChnahepwd();
                }
            }
        });
    }


    private void callChnahepwd() {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ChnagePassword.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.ChnagePassword + "username=" + emailStr + "&" + "password=" + passwordStr + "&" + "password1=" + password1;
            serviceCaller.CallCommanServiceMethod(url, "Login", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.setUserId(ChnagePassword.this, emailStr);
                                Toast.makeText(ChnagePassword.this, "Password Change Successfully.", Toast.LENGTH_LONG).show();
                                Intent intentLoggedIn = new Intent(ChnagePassword.this, LoginActivity.class);
                                startActivity(intentLoggedIn);
                            } else {
                                Toast.makeText(ChnagePassword.this, "Password not Change!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ChnagePassword.this, "Password not  Change Successfully!", Toast.LENGTH_LONG).show();
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

    // ----validation -----
    private boolean isValidate() {
        if (passwordStr.length() == 0) {
            showToast("Please enter password");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(ChnagePassword.this, message, Toast.LENGTH_LONG).show();
    }


    //for hid keyboard when tab outside edittext box
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}