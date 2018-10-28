package com.apitechnosoft.ipad.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.LoginActivity;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PricingFragment extends MainFragment {

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_pricing;
    }
    HeaderFragment headerFragment;
    TextView gb5, gb50, gb500;
    Button goldbt, silverbt;
    CheckBox silvermonth, silveryear, goldmonth, goldyear;
    String payAmount = "0";
    String UserId;
    // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    // or live (ENVIRONMENT_PRODUCTION)
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;

    // note that these credentials will differ between live & sandbox
    // environments.
//testing = AVZUbOX3ry-gyvTBVykh9TnK1v49hM0ycQiquryr8NjuRwnayplCFEm1M4ZnK5Q9JCcWzn5_briWUeRH
    // ipad=    AfPQXoihdmLg8g1nzNSZ5bvg09MSEqo50cbZkeO4_KDLgMkjR2oeyNIUuDMhNWdBYoRvIu7lGf4E7Lmk
    private static final String CONFIG_CLIENT_ID = "AfPQXoihdmLg8g1nzNSZ5bvg09MSEqo50cbZkeO4_KDLgMkjR2oeyNIUuDMhNWdBYoRvIu7lGf4E7Lmk";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    PayPalPayment thingToBuy;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // the following are only used in PayPalFuturePaymentActivity.
            .merchantName("ipad")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://ipadtoday.com/Privarcy.jsp"))
            .merchantUserAgreementUri(
                    Uri.parse("https://ipadtoday.com/TermsOfService.jsp"));

    @Override
    protected void loadView() {
        gb5 = findViewById(R.id.gb5);
        gb50 = findViewById(R.id.gb50);
        gb500 = findViewById(R.id.gb500);
        goldbt = findViewById(R.id.goldbt);
        silverbt = findViewById(R.id.silverbt);

        silvermonth = findViewById(R.id.silvermonth);
        silveryear = findViewById(R.id.silveryear);
        goldmonth = findViewById(R.id.goldmonth);
        goldyear = findViewById(R.id.goldyear);
    }

    @Override
    protected void setClickListeners() {
        goldbt.setOnClickListener(this);
        silverbt.setOnClickListener(this);
    }

    @Override
    protected void setAccessibility() {
    }

    @Override
    protected void dataToView() {
        getAllNotification();
        gb5.setText(Html.fromHtml("5<sup>GB</sup>"));
        gb50.setText(Html.fromHtml("50<sup>GB</sup>"));
        gb500.setText(Html.fromHtml("500<sup>GB</sup>"));
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
        }
        Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getContext().startService(intent);

        silvermonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    silveryear.setChecked(false);
                    goldmonth.setChecked(false);
                    goldyear.setChecked(false);

                }
            }
        });
        silveryear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    silvermonth.setChecked(false);
                    goldmonth.setChecked(false);
                    goldyear.setChecked(false);

                }
            }
        });
        goldmonth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    silveryear.setChecked(false);
                    silvermonth.setChecked(false);
                    goldyear.setChecked(false);

                }
            }
        });
        goldyear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    silveryear.setChecked(false);
                    silvermonth.setChecked(false);
                    goldmonth.setChecked(false);

                }
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goldbt:
                if (goldmonth.isChecked()) {
                    payAmount = "9.99";
                    callPayment();
                } else if (goldyear.isChecked()) {
                    payAmount = "99.90";
                    callPayment();
                } else {
                    ASTUIUtil.showToast("Please select anyone payment option in Gold User!");
                }
                break;
            case R.id.silverbt:

                if (silveryear.isChecked()) {
                    payAmount = "5.99";
                    callPayment();
                } else if (silvermonth.isChecked()) {
                    payAmount = "59.90";
                    callPayment();
                } else {
                    ASTUIUtil.showToast("Please select anyone payment option in Silver User!");
                }
                break;
        }
    }

    private void callPayment() {
        thingToBuy = new PayPalPayment(new BigDecimal(payAmount), "USD",
                "HeadSet", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getContext(),
                PaymentActivity.class);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(getContext(),
                PayPalFuturePaymentActivity.class);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject().toString(4));
                        paymentConfirm();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(getContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    public void onFuturePaymentPurchasePressed(View pressed) {
        // Get the Application Correlation ID from the SDK
        String correlationId = PayPalConfiguration
                .getApplicationCorrelationId(getContext());

        Log.i("FuturePaymentExample", "Application Correlation ID: "
                + correlationId);

        // TODO: Send correlationId and transaction details to your server for
        // processing with
        // PayPal...
        Toast.makeText(getContext(),
                "App Correlation ID received from SDK", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        getContext().stopService(new Intent(getContext(), PayPalService.class));
        super.onDestroy();
    }

    private void paymentConfirm() {
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar dotDialog = new ASTProgressBar(getContext());
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(getContext());
            final String url = Contants.BASE_URL + Contants.ServcePlanApi + "username=" + UserId + "&" + "price=" + payAmount;
            serviceCaller.CallCommanServiceMethod(url, "paymentConfirm", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                Toast.makeText(getContext(), "Your plan save successfully",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Your plan not save successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Your plan not save successfully!", Toast.LENGTH_LONG).show();
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
