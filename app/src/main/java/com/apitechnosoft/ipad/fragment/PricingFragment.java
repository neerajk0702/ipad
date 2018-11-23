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
import com.apitechnosoft.ipad.framework.FileUploaderHelper;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.model.Data;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;

import okhttp3.MultipartBody;

import static com.apitechnosoft.ipad.utils.ASTUIUtil.showToast;

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
    String UserId, fname, lname;
    final int REQUEST_CODE = 1;
    //   final String get_token = "http://192.168.1.12/braintreepayment/main.php";
    final String get_token = "https://www.ipadtoday.com/resources/BrainTreeImplementationApi/braintreeProcessingdata";
    final String send_payment_details = "https://www.ipadtoday.com/resources/BrainTreePaymentTransactionApi/braintreeTransaction";
    String token;


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
            fname = prefs.getString("FirstName", "");
            lname = prefs.getString("LastName", "");

        }
        getTokenPayment(UserId, fname, lname, payAmount);


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
                    payAmount = "59.90";
                    callPayment();
                } else if (silvermonth.isChecked()) {
                    payAmount = "5.99";
                    callPayment();
                } else {
                    ASTUIUtil.showToast("Please select anyone payment option in Silver User!");
                }
                break;
        }
    }

    private void callPayment() {
        onBraintreeSubmit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String stringNonce = nonce.getNonce();
                sendPaymentRansaction(UserId, fname, lname, payAmount, stringNonce);
                if (resultCode == Activity.RESULT_CANCELED) {
                } else {
                    Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                }
            }
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
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

    private void getTokenPayment(String emailid, String fanem, String lname, String ammount) {
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar progressBar = new ASTProgressBar(getContext());
            progressBar.show();
            final String serviceURL = get_token;

            JSONObject object = new JSONObject();
            try {
                object.put("email", emailid);
                object.put("fname", fanem);
                object.put("lname", lname);
                object.put("ammount", ammount);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("jsonData", object.toString());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(getContext(), payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            Toast.makeText(getContext(), "Successfully got token", Toast.LENGTH_SHORT).show();
                            token = data.getToken();


                        } else {
                            Toast.makeText(getContext(), "Failed to get token: ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }

    }


    private void sendPaymentRansaction(String emailid, String fanem, String lname, String ammount, String clientNonce) {
        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar progressBar = new ASTProgressBar(getContext());
            progressBar.show();
            final String serviceURL = send_payment_details;

            JSONObject object = new JSONObject();
            try {
                object.put("email", emailid);
                object.put("fname", fanem);
                object.put("lname", lname);
                object.put("ammount", ammount);
                object.put("clientNonce", clientNonce);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("jsonData", object.toString());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(getContext(), payloadList, multipartBody, serviceURL) {
                @Override
                public void receiveData(String result) {
                    if (result != null && !result.equals("") && !result.equals("{}")) {
                        Data data = new Gson().fromJson(result, Data.class);
                        if (data != null) {
                            if (data.getStatus().equalsIgnoreCase("Approved")) {
                                sendPaymentDetails(emailid, payAmount);
                            } else {
                                Toast.makeText(getContext(), data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed Payment ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Failed Payment ", Toast.LENGTH_LONG).show();
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }

    }


    public void onBraintreeSubmit() {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token);
        startActivityForResult(dropInRequest.getIntent(getContext()), REQUEST_CODE);
    }

    private void sendPaymentDetails(String name, String price) {

        if (ASTUIUtil.isOnline(getContext())) {
            final ASTProgressBar progressBar = new ASTProgressBar(getContext());
            progressBar.show();
            final String url = Contants.BASE_URL + Contants.SavePlan;

            JSONObject object = new JSONObject();
            try {
                object.put("userName", name);
                object.put("price", price);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            HashMap<String, String> payloadList = new HashMap<String, String>();
            payloadList.put("jsonData", object.toString());

            MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            FileUploaderHelper fileUploaderHelper = new FileUploaderHelper(getContext(), payloadList, multipartBody, url) {
                @Override
                public void receiveData(String result) {
                    if (result != null) {
                        Data data = new Gson().fromJson(result, Data.class);
                        if (data != null) {
                            if (data.getStatus().equals("true")) {
                                Toast.makeText(getContext(), "Payment Successfully done.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed Payment!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed Payment!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        showToast(Contants.Error);
                    }
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            };
            fileUploaderHelper.execute();
        } else {
            ASTUIUtil.alertForErrorMessage(Contants.OFFLINE_MESSAGE, getContext());//off line msg....
        }


    }


}
