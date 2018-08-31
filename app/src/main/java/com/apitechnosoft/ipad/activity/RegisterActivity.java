package com.apitechnosoft.ipad.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.mail.Mail;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.FontManager;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogIn;
    Typeface materialdesignicons_font;
    EditText edt_firstname, edt_lastname, edt_mail, edt_password, edt_otp;
    String firstnamestr, lastnamestr, mailstr, passwordstr;
    LinearLayout otpLayout;
    String PINString;
    boolean buttonFlag = false;
    private TextView welcom, Privacy,ipad;
    private CheckBox accept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loadView();
        datatoView();
    }

    //get view ids
    public void loadView() {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        edt_firstname = (EditText) findViewById(R.id.edt_firstname);
        edt_lastname = (EditText) findViewById(R.id.edt_lastname);
        edt_mail = (EditText) findViewById(R.id.edt_mail);
        edt_password = (EditText) findViewById(R.id.edt_password);
        otpLayout = findViewById(R.id.otpLayout);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        welcom = findViewById(R.id.welcom);
        Privacy = findViewById(R.id.Privacy);
        Privacy.setOnClickListener(this);
        welcom.setOnClickListener(this);
        accept = findViewById(R.id.accept);
        ipad = findViewById(R.id.ipad);
        ipad.setText(Html.fromHtml("Welcome to iPad<sup>TM</sup>"));

        TextView iicon = findViewById(R.id.iicon);
        iicon.setTypeface(materialdesignicons_font);
        iicon.setText(Html.fromHtml("&#xf2fd;"));
    }

    //get data from UI
    public void datatoView() {
        btnLogIn.setText("Send Verification Code");
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonFlag) {
                    if (isValidate()) {
                        String userOtp = edt_otp.getText().toString();
                        if (userOtp.length() != 0) {
                            if (PINString.equals(userOtp)) {
                                callSignup();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Verification Code did not matched!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Please Enter Verification Code!", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (isValidate()) {
                        new SendMail().execute("");
                    }
                }

            }
        });


    }

    private void getOTP() {
        int randomPIN = (int) (Math.random() * 9000) + 1000;
        PINString = String.valueOf(randomPIN);
    }

    private void callSignup() {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(RegisterActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.signup + "fName=" + firstnamestr + "&" + "lName=" + lastnamestr + "&" + "emailid=" + mailstr + "&" + "pwd=" + passwordstr;
            serviceCaller.CallCommanServiceMethod(url, "Signup", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.setUserId(RegisterActivity.this,mailstr);
                                Toast.makeText(RegisterActivity.this, "Signup Successfully.", Toast.LENGTH_LONG).show();
                                Intent intentLoggedIn = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intentLoggedIn);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Signup not Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Signup not Successfully!", Toast.LENGTH_LONG).show();
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
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        firstnamestr = edt_firstname.getText().toString();
        lastnamestr = edt_lastname.getText().toString();
        mailstr = edt_mail.getText().toString();
        passwordstr = edt_password.getText().toString();
        if (firstnamestr.length() == 0) {
            showToast("Please enter first name");
            return false;
        } else if (lastnamestr.length() == 0) {
            showToast("Please enter last name");
            return false;
        } else if (mailstr.length() == 0) {
            showToast("Please enter Email Id");
            return false;
        } else if (!mailstr.matches(emailRegex)) {
            showToast("Please enter valid Email ID");
            return false;
        } else if (passwordstr.length() == 0) {
            showToast("Please enter password");
            return false;
        } else if (!accept.isChecked()) {
            showToast("Please select Term's of Service.");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Privacy:
                Intent i = new Intent(RegisterActivity.this, PrivacyActivity.class);
                startActivity(i);
                break;
            case R.id.welcom:
                Intent i2 = new Intent(RegisterActivity.this, TermsConditionActivity.class);
                startActivity(i2);
                break;

        }
    }

    private class SendMail extends AsyncTask<String, Integer, Boolean> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RegisterActivity.this, "Please wait", "Sending Verification Code", true, false);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (aVoid) {
                    buttonFlag = true;
                    btnLogIn.setText("Submit");
                    otpLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "Verification Code was sent to your Mail successfully.", Toast.LENGTH_LONG).show();
                } else {
                    btnLogIn.setText("Send Verification Code");
                    otpLayout.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Verification Code was not sent to your Mail successfully.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            progressDialog.dismiss();
        }

        protected Boolean doInBackground(String... params) {
            getOTP();
            boolean doneflag = false;
            Mail m = new Mail("admin@rxdmedia.com", "Cowboys777!");

            String[] toArr = {mailstr};//{"neerajk0702@gmail.com", "89neerajsingh@gmail.com"};
            m.setTo(toArr);
            m.setFrom("admin@rxdmedia.com");
            m.setSubject("Ipad SignUp Verification Code");
            m.setBody("Your Verification Code is :" + PINString);

            try {
                if (m.send()) {
                    doneflag = true;
                } else {
                    doneflag = false;
                }
            } catch (Exception e) {
                //Log.e("MailApp", "Could not send email", e);
            }
            return doneflag;
        }
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
