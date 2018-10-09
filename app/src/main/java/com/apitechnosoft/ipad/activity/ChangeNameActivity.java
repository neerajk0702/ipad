package com.apitechnosoft.ipad.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;

import static com.apitechnosoft.ipad.utils.ASTUIUtil.showToast;

public class ChangeNameActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String UserId;
    EditText lname, fname;
    String fnamee, lnamee;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        if (ASTUtil.isTablet(ChangeNameActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
    }
    private void init(){
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
        fname = this.findViewById(R.id.fname);
        lname = this.findViewById(R.id.lname);
        submitButton = this.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fnamee = fname.getText().toString();
                lnamee = lname.getText().toString();
                if (ASTObjectUtil.isEmptyStr(fnamee)) {
                    ASTUIUtil.showToast("Please Enter First Name");
                } else if (ASTObjectUtil.isEmptyStr(lnamee)) {
                    ASTUIUtil.showToast("Please Enter Last Name");
                } else {
                    ChnageName();
                }
            }
        });

    }
    private void ChnageName() {
        if (ASTUIUtil.isOnline(ChangeNameActivity.this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(ChangeNameActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(ChangeNameActivity.this);
            final String url = Contants.BASE_URL + Contants.UpdateUserName + "username=" + UserId + "&" + "fname=" + fnamee + "&" + "lname=" + lnamee;
            serviceCaller.CallCommanServiceMethod(url, "Login", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                Toast.makeText(ChangeNameActivity.this, "Name Chnage Successfully.", Toast.LENGTH_LONG).show();
                               finish();
                            } else {
                                Toast.makeText(ChangeNameActivity.this, "Not Updated Name!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ChangeNameActivity.this, "Name not  Update Successfully!", Toast.LENGTH_LONG).show();
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
