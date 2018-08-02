package com.apitechnosoft.ipad.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Constant;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.utils.ASTUIUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    Button btnLogIn;
    EditText etUserName, etPassword;
    ASTUIUtil commonFunctions;
    String userId = "";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadView();
        datatoView();
    }

    //get view ids
    public void loadView() {
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPass);
    }

    //get data from UI
    public void datatoView() {
        commonFunctions = new ASTUIUtil();
        pref = getApplicationContext().getSharedPreferences("SharedPref", MODE_PRIVATE);
        userId = pref.getString("USER_ID", "");
        if (!userId.equals("")) {
            Intent intentHome = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentHome);
        }

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                Boolean connected = commonFunctions.checkNetwork(LoginActivity.this);
                if (userName.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Provide Username", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please Provide Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (ASTUIUtil.isOnline(LoginActivity.this)) {
                        final ASTProgressBar dotDialog = new ASTProgressBar(LoginActivity.this);
                        dotDialog.show();
                        String serviceURL = Constant.BASE_URL + Constant.LOGIN_URL;
                        serviceURL += "" + userName + "/" + password;
                        //   WebServiceCall webServices = new WebServiceCall();
                        //   webServices.login(serviceURL, LoginActivity.this);
                        ServiceCaller serviceCaller = new ServiceCaller(LoginActivity.this);
                        serviceCaller.CallCommanServiceMethod(serviceURL, "login", new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String result, boolean isComplete) {
                                if (isComplete) {
                                    parseLoginServiceData(result);
                                } else {
                                    ASTUIUtil.alertForErrorMessage(Contants.Error, LoginActivity.this);
                                }
                                if (dotDialog.isShowing()) {
                                    dotDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, LoginActivity.this);//off line msg....
                    }

                }
            }
        });

    }

    /*
     *
     * Parse and Validate Login Service Data
     */
    private void parseLoginServiceData(String result) {
        if (result != null) {
            try {
                pref = this.getApplicationContext().getSharedPreferences("SharedPref", MODE_PRIVATE);
                JSONObject jsonRootObject = new JSONObject(result);
                String jsonStatus = jsonRootObject.optString("Status").toString();
                if (jsonStatus.equals("1")) {
                    JSONArray jsonArray = jsonRootObject.optJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userName = jsonObject.optString("UserName").toString();
                        String userId = jsonObject.optString("UserId").toString();
                        String Name = jsonObject.optString("Name").toString();
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("USER_NAME", userName);
                        editor.putString("USER_ID", userId);
                        editor.putString("EMP_NAME", Name);
                        editor.commit();
                        Intent intentHomeScreen = new Intent(this, MainActivity.class);
                        this.startActivity(intentHomeScreen);
                    }
                } else {
                    Toast.makeText(this, "Please Provide Correct Credentials", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }

    }
}