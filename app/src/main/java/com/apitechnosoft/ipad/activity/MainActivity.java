package com.apitechnosoft.ipad.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.ApplicationHelper;
import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTProgressBar;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.fragment.AboutFragment;
import com.apitechnosoft.ipad.fragment.ContactUsFragment;
import com.apitechnosoft.ipad.fragment.EducationFragment;
import com.apitechnosoft.ipad.fragment.HeaderFragment;
import com.apitechnosoft.ipad.fragment.HomeFragment;
import com.apitechnosoft.ipad.fragment.MainFragment;
import com.apitechnosoft.ipad.fragment.MyProfileFragment;
import com.apitechnosoft.ipad.fragment.PressFragment;
import com.apitechnosoft.ipad.fragment.PricingFragment;
import com.apitechnosoft.ipad.framework.IAsyncWorkCompletedCallback;
import com.apitechnosoft.ipad.framework.ServiceCaller;
import com.apitechnosoft.ipad.model.ContentResponce;
import com.apitechnosoft.ipad.runtimepermission.PermissionResultCallback;
import com.apitechnosoft.ipad.runtimepermission.PermissionUtils;
import com.apitechnosoft.ipad.utils.ASTReqResCode;
import com.apitechnosoft.ipad.utils.ASTUIUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.apitechnosoft.ipad.ApplicationHelper.application;
import static com.apitechnosoft.ipad.utils.ASTObjectUtil.isNonEmptyStr;
import static com.apitechnosoft.ipad.utils.ASTUIUtil.showToast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

    NavigationView navigationView;
    ArrayList<String> permissions = new ArrayList<>();
    PermissionUtils permissionUtils;
    private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private int REQUEST_CODE_GPS_PERMISSIONS = 2;
    String UserId, FirstName, LastName;
    TextView loginUsrName, loginUserEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        application().setActivity(this);
        navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        loginUsrName = headerLayout.findViewById(R.id.loginUsrName);
        loginUserEmailId = headerLayout.findViewById(R.id.loginUserEmailId);
        loadPage();
        showNavigationMenuItem();
        runTimePermission();
        getUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        application().setActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (this.closeDrawers()) {
            return;
        }
        if (this.getPageFragment() != null && this.getPageFragment().onBackPressed()) {
            if (this.getSupportFragmentManager().getBackStackEntryCount() == 1) {
                redirectToHomeMenu();
            } else {
                super.onBackPressed();
            }
        }
    }

    protected void loadPage() {
        MenuItem menuItem = navigationView.getMenu().getItem(0);
        Bundle pageBundle = new Bundle();
        pageBundle.putString("headerTxt", "Home");
        pageBundle.putInt("MENU_ID", 0);
        application().lastMenuItem = menuItem;
        this.updateFragment(new HomeFragment(), pageBundle);
    }

    protected
    @IdRes
    int dataContainerResID() {
        try {
            return R.id.dataContainer;
        } catch (NoSuchFieldError e) {
            return 0;
        }
    }

    protected
    @IdRes
    int headerViewResID() {
        try {
            return R.id.headerFragment;
        } catch (NoSuchFieldError e) {
            return 0;
        }
    }

    protected
    @IdRes
    int navigationViewResID() {
        try {
            return R.id.nav_view;
        } catch (NoSuchFieldError e) {
            return 0;
        }
    }

    protected
    @IdRes
    int drawerLayoutResID() {
        try {
            return R.id.drawer_layout;
        } catch (NoSuchFieldError e) {
            return 0;
        }
    }


    @SuppressLint("ResourceType")
    public DrawerLayout drawerLayout() {
        if (dataContainerResID() > 0) {
            return (DrawerLayout) this.findViewById(drawerLayoutResID());
        }
        return null;
    }

    @SuppressLint("ResourceType")
    public View headerView() {
        if (headerViewResID() > 0) {
            return this.findViewById(headerViewResID());
        }
        return null;
    }

    @SuppressLint("ResourceType")
    public View navigationView() {
        if (navigationViewResID() > 0) {
            return this.findViewById(navigationViewResID());
        }
        return null;
    }

    public HeaderFragment headerFragment() {
        if (headerView() == null) {
            return null;
        }
        return (HeaderFragment) this.findFragmentById(headerViewResID());
    }

    public Fragment findFragmentById(int fragmentContainerID) {
        return this.getSupportFragmentManager().findFragmentById(fragmentContainerID);
    }

    protected MainFragment headerFragmentInstance() {
        return new HeaderFragment();
    }

    public void setDrawerState(boolean isEnabled) {
        if (this.drawerLayout() == null) {
            return;
        }
        if (isEnabled) {
            this.drawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            this.drawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public boolean closeDrawers() {
        if (this.drawerLayout() != null && this.drawerLayout().isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout().closeDrawers();
            return true;
        }
        return false;
    }

    public void showSideNavigationPanel() {
        if (drawerLayout() == null) {
            return;
        }
        if (this.drawerLayout().isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout().closeDrawer(GravityCompat.START);
        } else {
            this.hideKeyBoard();
            this.drawerLayout().openDrawer(GravityCompat.START);
        }
    }

    public void hideKeyBoard() {
        ASTUIUtil.hideKeyboard(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        bundle.putInt("MENU_ID", item.getItemId());
        if (id == R.id.nav_home) {
            bundle.putString("headerTxt", "Home");
            bundle.putInt("MENU_ID", 0);
            this.updateFragment(new HomeFragment(), bundle);
        } else if (id == R.id.nav_About) {
            bundle.putString("headerTxt", "About Us");
            this.updateFragment(new AboutFragment(), bundle);
        } else if (id == R.id.nav_Pricing) {
            bundle.putString("headerTxt", "Pricing");
            this.updateFragment(new PricingFragment(), bundle);
        } else if (id == R.id.nav_Education) {
            bundle.putString("headerTxt", "Education");
            this.updateFragment(new EducationFragment(), bundle);
        } else if (id == R.id.nav_Press) {
            bundle.putString("headerTxt", "Press");
            this.updateFragment(new PressFragment(), bundle);
        } else if (id == R.id.nav_Contact) {
            bundle.putString("headerTxt", "Contact Us");
            this.updateFragment(new ContactUsFragment(), bundle);
        } else if (id == R.id.nav_Terms) {
            Intent i = new Intent(MainActivity.this, TermsConditionActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_Privacy) {
            Intent i = new Intent(MainActivity.this, PrivacyActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_Logout) {
            SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            if (prefs != null) {
                prefs.edit().clear().commit();
            }
            Intent i = new Intent(MainActivity.this, LoginHomeActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_profile) {
            bundle.putString("headerTxt", "My Profile");
            this.updateFragment(new MyProfileFragment(), bundle);
        }
        DrawerLayout drawer = this.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void updateFragment(MainFragment pageFragment, Bundle bundle) {
        this.updateFragment(pageFragment, bundle, true, false);
    }


    public void updateFragment(MainFragment pageFragment, Bundle bundle, boolean replaceHeaderFragment, boolean animate) {
        this.hideKeyBoard();
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        String menuId = null;
        MainFragment headerFragment = replaceHeaderFragment ? headerFragmentInstance() : null;
        if (bundle != null) {
            pageFragment.setArguments(bundle);
            if (headerFragment != null) {
                headerFragment.setArguments(bundle);
            }
            menuId = String.valueOf(bundle.getInt("MENU_ID", 0));
        }
        if (isNonEmptyStr(menuId) && (!menuId.equalsIgnoreCase("0"))) {
            fragmentTransaction.addToBackStack(menuId);
            fragmentTransaction.replace(dataContainerResID(), pageFragment, menuId);
        } else {
            fragmentTransaction.addToBackStack(null);
            if (animate) {
                fragmentTransaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            }
            if (pageFragment.getTargetFragment() == null) {
                pageFragment.setTargetFragment(getPageFragment(), 101);
            }
            fragmentTransaction.replace(dataContainerResID(), pageFragment);
        }

        //application().setLastPageID(pageFragment.getClass().getSimpleName());
        if (headerFragment != null) {
            fragmentTransaction.replace(headerViewResID(), headerFragment);
        }
        commitTransation(fragmentTransaction);
    }

    public MainFragment getPageFragment() {
        return dataContainer();
    }

    public MainFragment dataContainer() {
        Fragment fragment = this.findFragmentById(dataContainerResID());
        return fragment != null ? (MainFragment) fragment : null;
    }

    protected void commitTransation(FragmentTransaction fragmentTransaction) {
        if (application().isAppInForground()) {
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.commitAllowingStateLoss();
        }
        this.closeDrawers();
    }

    public void requestPermission(int permissionCode) {
        this.requestPermission(permissionCode, permissionCode);
    }

    public boolean isPermissionGranted(int permissionCode) {
        return isPermissionGranted(Manifest.permission.CALL_PHONE);
    }

    public boolean isPermissionGranted(String permission) {
        int rc = ActivityCompat.checkSelfPermission(this, permission);
        return rc == PackageManager.PERMISSION_GRANTED;
    }

    public void permissionGranted(int requestCode) {
        if (getPageFragment() != null) {
            getPageFragment().permissionGranted(requestCode);
        }
    }

    public void permissionDenied(int requestCode) {
        if (getPageFragment() != null) {
            getPageFragment().permissionDenied(requestCode);
        }
    }

    /**
     * Request for permission
     * If permission denied or app is first launched, request for permission
     * If permission denied and user choose 'Nerver Ask Again', show snackbar with an action that navigate to app settings
     */
    public void requestPermission(int permissionCode, int actionCode) {
        String permission = permissionForCode(permissionCode);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || isPermissionGranted(permission)) {
            this.permissionGranted(actionCode);
            return;
        }
        final String[] permissions = new String[]{permission};
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            ActivityCompat.requestPermissions(this, permissions, actionCode);
        } else {
            String permissionPref = permissionPref(permissionCode);
            if (!application().isPermissionRequested(permissionPref)) {
                application().setPermissionRequested(permissionPref);
                ActivityCompat.requestPermissions(this, permissions, actionCode);
            } else {
                // askUsertoChangePermission(messageIdForPermission(permissionCode));
            }
        }
    }


    private String permissionPref(int permissionCode) {
        switch (permissionCode) {
            case 508:
                return "callPhoneRequested";
        }

        return "PERMISSION_REQ_" + permissionCode;
    }

  /*  protected void askUsertoChangePermission(@StringRes int message) {
        AlertDialog deviceSettingsDialog = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.openSett, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        deviceSettingsDialog.setTitle(R.string.access_denied);
        deviceSettingsDialog.setMessage(ASTStringUtil.getStringForID(message));
        deviceSettingsDialog.show();
    }*/

    protected String permissionForCode(int permissionCode) {
        switch (permissionCode) {
            case ASTReqResCode.PERMISSION_REQ_ACCESS_COARSE_LOCATION:
                return Manifest.permission.ACCESS_COARSE_LOCATION;
            case ASTReqResCode.PERMISSION_REQ_ACCESS_FINE_LOCATION:
                return Manifest.permission.ACCESS_FINE_LOCATION;
            case ASTReqResCode.PERMISSION_REQ_CAMERA:
                return Manifest.permission.CAMERA;
            case ASTReqResCode.PERMISSION_REQ_READ_CONTACTS:
                return Manifest.permission.READ_CONTACTS;
            case ASTReqResCode.PERMISSION_REQ_READ_PHONE_STATE:
                return Manifest.permission.READ_PHONE_STATE;
            case ASTReqResCode.PERMISSION_REQ_RECORD_AUDIO:
                return Manifest.permission.RECORD_AUDIO;
            case ASTReqResCode.PERMISSION_REQ_WRITE_CALENDAR:
                return Manifest.permission.WRITE_CALENDAR;
            case ASTReqResCode.PERMISSION_REQ_WRITE_EXTERNAL_STORAGE:
                return Manifest.permission.WRITE_EXTERNAL_STORAGE;
            case ASTReqResCode.PERMISSION_REQ_CALL_PHONE:
                return Manifest.permission.CALL_PHONE;
        }

        return null;
    }


    /**
     * redirect to Home menu Navigation item
     */
    public void redirectToHomeMenu() {
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.getPageFragment() != null) {
            this.getPageFragment().updateOnResult(requestCode, resultCode, data);
        }
    }

    SharedPreferences pref;
    String userName;
    String[] menuIds;

    public void showNavigationMenuItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
    }

    //---------Run time permission---------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void runTimePermission() {
        permissionUtils = new PermissionUtils(MainActivity.this);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.CALL_PHONE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissions.add(Manifest.permission.READ_CALL_LOG);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.WAKE_LOCK);
        //permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        permissions.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);


        permissionUtils.check_permission(permissions, "Location, Phone and Storage Services Permissions are required for this App.", REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
    }

    @Override
    public void PermissionGranted(int request_code) {
        checkGpsEnable();
        //for
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
        //finish();
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
        //  finish();
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
        neverAskAgainAlert();
    }


    private void neverAskAgainAlert() {
        //Previously Permission Request was cancelled with 'Dont Ask Again',
        // Redirect to Settings after showing Information about why you need the permission
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setCancelable(false);
        builder.setMessage("Location, Phone and Storage Services Permissions are required for this App.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", ApplicationHelper.application().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                // finish();
            }
        });
        builder.show();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alert = builder.create();
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        alert.dismiss();
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_CODE_GPS_PERMISSIONS);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        alert.dismiss();
                        checkGpsEnable();
                    }
                });

        alert.show();
    }

    //    check gps enable in device or not
    private void checkGpsEnable() {
        try {
            boolean isGPSEnabled = false;
            boolean isNetworkEnabled = false;
            final LocationManager locationManager = (LocationManager) ApplicationHelper.application().getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                buildAlertMessageNoGps();
            }

        } catch (Exception e) {
            e.printStackTrace();
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


    private void getUserInfo() {

        SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (prefs != null) {
            UserId = prefs.getString("UserId", "");
            FirstName = prefs.getString("FirstName", "");
            LastName = prefs.getString("LastName", "");
            loginUsrName.setText(FirstName + " " + LastName);
            loginUserEmailId.setText(UserId);
        }

        if (ASTUIUtil.isOnline(this)) {
            final ASTProgressBar dotDialog = new ASTProgressBar(MainActivity.this);
            // dotDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(this);
            final String url = Contants.BASE_URL + Contants.UserInfoService + "email=" + UserId;
            serviceCaller.CallCommanServiceMethod(url, "getUserInfo", new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String result, boolean isComplete) {
                    if (isComplete) {
                        ContentResponce data = new Gson().fromJson(result, ContentResponce.class);
                        if (data != null) {
                            if (data.getUser() != null) {
                                SharedPreferences prefs = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("FirstName", data.getUser().getfName());
                                editor.putString("LastName", data.getUser().getlName());
                                editor.commit();
                            }
                        }
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

}
