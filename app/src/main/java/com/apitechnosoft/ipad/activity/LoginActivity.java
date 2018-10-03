package com.apitechnosoft.ipad.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    Button btnLogIn;
    Typeface materialdesignicons_font;
    EditText edt_phone, edt_password;
    String passwordStr, emailStr;
    TextView welcom, forgotPasssword;
    public final String SiteKey = "6LfX9XIUAAAAABxYNoXqqajjwCVNg-tqyZtrc2yp";
    public final String SiteSecretKey = "6LfX9XIUAAAAAKf_nuYjGe71TOhrZF6IbFv_4eFt";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String URL_VERIFY_ON_SERVER = Contants.BASE_URL + Contants.VerifyRecaptchaApi;
    private GoogleApiClient mGoogleApiClient;
    ASTProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ASTUtil.isTablet(LoginActivity.this)) {
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
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_phone = (EditText) findViewById(R.id.edt_phone);

        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        TextView email_icon = (TextView) findViewById(R.id.email_icon);
        email_icon.setTypeface(materialdesignicons_font);
        email_icon.setText(Html.fromHtml("&#xf1ee;"));
        TextView password_icon = (TextView) findViewById(R.id.password_icon);
        password_icon.setTypeface(materialdesignicons_font);
        password_icon.setText(Html.fromHtml("&#xf33e;"));
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
                    callLogin();
                }
            }
        });
        forgotPasssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForForgotPassword(LoginActivity.this);
            }
        });
        //sendMail("89neerajsingh@gmail.com", "Mail test", "Device mail");
    }


    public void alertForForgotPassword(final Context context) {
        final View myview = LayoutInflater.from(context).inflate(R.layout.forgot_password_layout, null);
        EditText edt_phone = myview.findViewById(R.id.edt_phone);
        Button btnLogIn = myview.findViewById(R.id.btnLogIn);
        Button btncancel = myview.findViewById(R.id.btncancel);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).setIcon(R.drawable.audio_icon).setCancelable(false)
                .setView(myview).create();
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (ASTUIUtil.isOnline(context)) {
                    validateCaptcha();
                    //  Intent intent = new Intent(LoginActivity.this, ChnagePassword.class);
                    // startActivity(intent);
                }
            }


        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void callLogin() {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(LoginActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.Login + "emailid=" + emailStr + "&" + "pwd=" + passwordStr;
            serviceCaller.CallCommanServiceMethod(url, "Login", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.setUserId(LoginActivity.this, emailStr, passwordStr);
                                Toast.makeText(LoginActivity.this, "Login Successfully.", Toast.LENGTH_LONG).show();
                                Intent intentLoggedIn = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intentLoggedIn);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login not Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login not Successfully!", Toast.LENGTH_LONG).show();
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
        passwordStr = edt_password.getText().toString();
        emailStr = edt_phone.getText().toString();

        if (emailStr.length() == 0) {
            showToast("Please enter Email Id");
            return false;
        } else if (!emailStr.matches(emailRegex)) {
            showToast("Please enter valid Email ID");
            return false;
        } else if (passwordStr.length() == 0) {
            showToast("Please enter password");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }


   /* String username = "neerajk0702@gmail.com";
    String name = "neeraj";
    String password = "scdscdscc";

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody,
                    session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Message createMessage(String email, String subject,
                                  String messageBody, Session session) throws MessagingException,
            UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, name));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,
                                "Mail sent successfully", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            } catch (final MessagingException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,
                                e.getClass() + " : " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }*/

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


    public void validateCaptcha() {
        progressBar = new ASTProgressBar(LoginActivity.this);
        progressBar.show();
        // Showing reCAPTCHA dialog
        SafetyNet.getClient(this).verifyWithRecaptcha(SiteKey)
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        Log.d(TAG, "onSuccess");
                        if (!response.getTokenResult().isEmpty()) {
                            //verifyTokenOnServer(response.getTokenResult());
                            forgotPasswordSendResponceToken(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.getMessage());
                        }
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                });
    }

    /**
     * Verifying the captcha token on the server
     * Post param: recaptcha-response
     * Server makes call to https://www.google.com/recaptcha/api/siteverify
     * with SECRET Key and Captcha token
     */
    public void verifyTokenOnServer(final String token) {
        Log.d(TAG, "Captcha Token" + token);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_VERIFY_ON_SERVER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        String message = jsonObject.getString("message");
                        ServiceCaller serviceCaller = new ServiceCaller(LoginActivity.this);
                        final String url = Contants.BASE_URL + Contants.ForgetPassword + "emailid=" + emailStr + "&" + "RecaptchaResponse=" + "";
                        serviceCaller.CallCommanServiceMethod(url, "Login", new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String result, boolean isComplete) {
                                if (isComplete) {
                                    ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                                    if (data != null) {
                                        if (data.isStatus()) {
                                            Intent intentLoggedIn = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intentLoggedIn);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Something went wrong..!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Something went wrong..!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    showToast(Contants.Error);
                                }
                                if (progressBar.isShowing()) {
                                    progressBar.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                        if (progressBar.isShowing()) {
                            progressBar.dismiss();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("recaptcha-response", token);
                return params;
            }
        };

        ApplicationHelper.application().addToRequestQueue(strReq);
    }

    private void forgotPasswordSendResponceToken(String responseCode) {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(LoginActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.CallCommanServiceMethod(URL_VERIFY_ON_SERVER, "forgotPasswordSendResponceToken", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                Toast.makeText(LoginActivity.this, "Password reset link send into your registered Mailid.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, data.getError_msg(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, data.getError_msg(), Toast.LENGTH_LONG).show();
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
