package com.apitechnosoft.ipad.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.fragment.HomeFragment;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.apitechnosoft.ipad.utils.ASTUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.facebook.appevents.UserDataStore.EMAIL;

public class LoginHomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    boolean isTab;
    LoginButton facebooklogin;
    CallbackManager callbackManager;
    SignInButton btn_gsign_in;
    Button instabt, linkedinbt;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 7;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String AUTH_TYPE = "rerequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        if (ASTUtil.isTablet(LoginHomeActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isTab = true;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isTab = false;
        }
        loadView();
        datatoView();
    }

    //get view ids
    public void loadView() {
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signup);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        instabt = findViewById(R.id.instabt);
        linkedinbt = findViewById(R.id.linkedinbt);
        instabt.setOnClickListener(this);
        linkedinbt.setOnClickListener(this);
        LinearLayout joinLayout = findViewById(R.id.joinLayout);
        joinLayout.setOnClickListener(this);
        facebooklogin = findViewById(R.id.facebooklogin);
        btn_gsign_in = findViewById(R.id.btn_gsign_in);
        btn_gsign_in.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btn_gsign_in.setSize(SignInButton.SIZE_STANDARD);

    }

    //get data from UI
    public void datatoView() {
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("EMAIL","public_profile"));
        facebooklogin.setReadPermissions(Arrays.asList(EMAIL, "public_profile"));
        //  facebooklogin.setAuthType(AUTH_TYPE);
        facebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("onSuccess");
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);

                        String emailid = bFacebookData.getString("email");
                        String fname = bFacebookData.getString("first_name");
                        String profile_pic = bFacebookData.getString("profile_pic");

                        callwithSocialMediaRegister(emailid, fname, profile_pic);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gsign_in:
                signIn();
                break;
            case R.id.login:
                Intent i = new Intent(LoginHomeActivity.this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.signup:
                Intent intent = new Intent(LoginHomeActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.joinLayout:
                Intent intentjoin = new Intent(LoginHomeActivity.this, RegisterActivity.class);
                startActivity(intentjoin);
                break;
            case R.id.instabt:
                signIn();
                break;
            case R.id.linkedinbt:
                linkedInLogin();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            Uri personPhotoUrl = null;
            if (acct.getPhotoUrl() != null) {
                personPhotoUrl = acct.getPhotoUrl();
            }
            String email = acct.getEmail();
            callwithSocialMediaRegister(email, personName, personPhotoUrl + "");
            Log.e(TAG, "Name: " + personName + ", email: " + email);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
           // handleSignInResult(result);
        } else {
            //   showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    //--------------linkdin login--------------------
    private void linkedInLogin() {
        LISessionManager.getInstance(getApplicationContext()).init(LoginHomeActivity.this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                getLinkedData();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.d(TAG, "error:" + error.toString());
            }
        }, true);
    }

    // Build the list of member permissions our LinkedIn session requires
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS, Scope.W_SHARE);
    }

    private void getLinkedData() {
        String url = "https://api.linkedin.com/v1/people/~:(id,email-address,picture-url,first-name,last-name)";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                Log.d(TAG, "apiResponse:" + apiResponse.toString());
                if (apiResponse != null) {
                    try {
                        String pimage = "";
                        String lastName = "";
                        JSONObject jsonObject = new JSONObject(apiResponse.toString());
                        String resStr = jsonObject.getString("responseData");
                        JSONObject resObj = new JSONObject(resStr);
                        String email = resObj.getString("emailAddress");
                        String firstName = resObj.getString("firstName");
                        if (resObj.has("lastName")) {
                            lastName = resObj.getString("lastName");
                        }
                        if (resObj.has("picture")) {
                            pimage = resObj.getString("picture");
                        }
                        String name = firstName + " " + lastName;
                        callwithSocialMediaRegister(email, name, pimage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
            }
        });
    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
        }
        return null;
    }

    private void callwithSocialMediaRegister(String emailStr, String fname, String pimage) {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(LoginHomeActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.SocialMediaRegistration + "emailid=" + emailStr + "&" + "fname=" + fname;
            serviceCaller.CallCommanServiceMethod(url, "Registeration", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.setUserId(LoginHomeActivity.this, emailStr, null, fname, null);
                                Toast.makeText(LoginHomeActivity.this, "Registeration Successfully.", Toast.LENGTH_LONG).show();
                                Intent intentLoggedIn = new Intent(LoginHomeActivity.this, MainActivity.class);
                                startActivity(intentLoggedIn);

                            } else {
                                // Toast.makeText(LoginHomeActivity.this, "Registeration not Successfully!", Toast.LENGTH_LONG).show();

                                if (data.getError_msg().equalsIgnoreCase("User email already Registr")) {
                                    callwithSocialMediaLogin(emailStr, fname);
                                }

                            }
                        } else {
                            // Toast.makeText(LoginHomeActivity.this, "Registeration not Successfully!", Toast.LENGTH_LONG).show();
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

    private void callwithSocialMediaLogin(String emailStr, String fname) {
        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(LoginHomeActivity.this);
            dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.SocialMediaLogin + "username=" + emailStr;
            serviceCaller.CallCommanServiceMethod(url, "Login", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.isStatus()) {
                                ASTUIUtil.setUserId(LoginHomeActivity.this, emailStr, null, fname, null);
                                Toast.makeText(LoginHomeActivity.this, "Login Successfully.", Toast.LENGTH_LONG).show();
                                Intent intentLoggedIn = new Intent(LoginHomeActivity.this, MainActivity.class);
                                startActivity(intentLoggedIn);


                            } else {
                                Toast.makeText(LoginHomeActivity.this, "Login not Successfully!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginHomeActivity.this, "Login not Successfully!", Toast.LENGTH_LONG).show();
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

    //gmail logout
    private void signOut() {
        LoginManager.getInstance().logOut();
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
}