package com.apitechnosoft.ipad;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.google.android.gms.security.ProviderInstaller;
import com.apitechnosoft.ipad.exception.FNExceptionUtil;
import com.apitechnosoft.ipad.framework.LruBitmapCache;
import com.apitechnosoft.ipad.utils.ASTConstants;
import com.apitechnosoft.ipad.utils.ASTEnum;
import com.apitechnosoft.ipad.utils.ASTObjectUtil;

/**
 * @author AST Inc.
 */
public class ApplicationClass extends Application {

    ActivityLifecycleHandler activityLifecycleHandler;
    private Typeface _sTypeface;
    public volatile MenuItem lastMenuItem;
    public volatile FNRequest lastRequest;
    private MainActivity _activity;
    private SharedPreferences sharedPref;
    private String packageName;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Typeface _fontRegular;
    private Typeface _fontBold;
    private Typeface _fontItalic;
    private Typeface _fontBoldItalic;
    private Typeface _fontSemiBold;
    private Typeface _fontSemiBoldItalic;
    private Typeface _fontLightItalic;
    private Typeface _fontExtraLightItalic;

    public static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        setAppInstance();
        this.init();
    }

    protected void setAppInstance() {
        ApplicationHelper.setApplicationObj(this);
    }

    private void init() {
        initActivityLifeCycleHandler();
        // Installing a newer security provider using Google Play Services to support
        // TLS v1.1 on Java 1.5 (API 15 or lower devices).
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (Exception e) {
        }
    }

    public void initIcons() {
        this._sTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf");

    }

    public Typeface getIconTypeFace() {
        if (this._sTypeface == null) {
            this.initIcons();
        }
        return this._sTypeface;
    }

    public boolean isClickedMenuIsPrimary() {
        return lastMenuItem != null;
    }

    public Context getContext() {
        return this.getApplicationContext();
    }


    public boolean isAppInForground() {
        return initActivityLifeCycleHandler().isINForground();
    }

    private ActivityLifecycleHandler initActivityLifeCycleHandler() {
        if (activityLifecycleHandler == null) {
            activityLifecycleHandler = new ActivityLifecycleHandler();
            registerActivityLifecycleCallbacks(activityLifecycleHandler);
        }
        return activityLifecycleHandler;
    }

    public MainActivity getActivity() {
        return this._activity;
    }

    public void setActivity(MainActivity activity) {
        this._activity = activity;
    }

    public SharedPreferences sharedPreferences() {
        if (this.sharedPref == null) {
            this.sharedPref = this.sharedPrefForKey("FitterShub.com");
        }
        return this.sharedPref;
    }

    private SharedPreferences sharedPrefForKey(String key) {
        return getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    /**
     * Set a permission is requested
     */
    public void setPermissionRequested(String permissionPref) {
        persistBoolean(permissionPref, true);
    }

    public void persistBoolean(String key, boolean value) {
        this.sharedPreferences().edit().putBoolean(key, value).apply();
    }

    public boolean getPersistedBoolean(String key, boolean defaultValue) {
        return this.sharedPreferences().getBoolean(key, defaultValue);
    }

    /**
     * Check if a permission is requestted or not (false by default)
     */
    public boolean isPermissionRequested(String permissionPref) {
        return getPersistedBoolean(permissionPref, false);
    }

    public boolean isPermissionGranted(int permissionCode) {
        return getActivity() != null && getActivity().isPermissionGranted(permissionCode);
    }

    //---------------for volley -------------
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public int getResourceId(String resourceName, String defType) {
        return getResourceId(resourceName, defType, this.packageName());
    }

    public int getResourceId(String resourceName, String defType, String packageName) {
        return (!ASTObjectUtil.isNumber(resourceName) && ASTObjectUtil.isNonEmptyStr(resourceName)) ? this.getResources().getIdentifier(resourceName, defType, packageName) : 0;
    }

    public String packageName() {
        if (this.packageName == null) {
            PackageInfo info = this.packageInfo();
            this.packageName = info != null ? info.packageName : "com.android.foundation";
        }
        return this.packageName;
    }

    public PackageInfo packageInfo() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            FNExceptionUtil.logException(e2, getApplicationContext());
        }
        return info;
    }


    public FNRequest initGetRequest(String url) {
        return new FNRequest(true, url);
    }

    public FNRequest initRequest(String actionID, View v) {
        return initRequest(actionID, v, null);
    }

    public FNRequest initRequest(String actionID, View v, Object headerID) {
        this.lastRequest = new FNRequest(actionID, v, headerID);
        return this.lastRequest;
    }

    public FNRequest initRequest(String actionID, FNView v) {
        return initRequest(actionID, v, null);
    }

    public FNRequest initRequest(String actionID, FNView v, Object headerID) {
        this.lastRequest = new FNRequest(actionID, v, headerID);
        return this.lastRequest;
    }

    public Typeface getFontTypeFace(ASTEnum fontType) {
        switch (fontType) {
            case FONT_BOLD:
                if (this._fontBold == null) {
                    initFont(fontType);
                }
                return this._fontBold;
            case FONT_ITALIC:
                if (this._fontItalic == null) {
                    initFont(fontType);
                }
                return this._fontItalic;
            case FONT_BOLD_ITALIC:
                if (this._fontBoldItalic == null) {
                    initFont(fontType);
                }
                return this._fontBoldItalic;
            case FONT_SEMIBOLD:
                if (this._fontSemiBold == null) {
                    initFont(fontType);
                }
                return this._fontSemiBold;
            case FONT_SEMIBOLD_ITALIC:
                if (this._fontSemiBoldItalic == null) {
                    initFont(fontType);
                }
                return this._fontSemiBoldItalic;
            case FONT_LIGHT_ITALIC:
                if (this._fontLightItalic == null) {
                    initFont(fontType);
                }
                return this._fontLightItalic;
            case FONT_EXTRALIGHT_ITALIC:
                if (this._fontExtraLightItalic == null) {
                    initFont(fontType);
                }
                return this._fontExtraLightItalic;
            default:
                if (this._fontRegular == null) {
                    initFont(fontType);
                }
                return this._fontRegular;
        }
    }

    public String resourceNameById(int resourceId) {
        String resourceName = "Unable to find resource ID";
        try {
            return this.getApplicationContext().getResources().getResourceEntryName(resourceId);
        } catch (Exception e) {

        }
        return resourceName;
    }

    public Object selectedObject() {
        return null;
    }

    public void initFont(ASTEnum fontType) {
        switch (fontType) {
            case FONT_BOLD:
                this._fontBold = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Bold);
                break;
            case FONT_BOLD_ITALIC:
                this._fontBoldItalic = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Bold_Italic);
                break;
            case FONT_ITALIC:
                this._fontItalic = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Italic);
                break;
            case FONT_SEMIBOLD:
                this._fontSemiBold = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Semi_Bold);
                break;
            case FONT_SEMIBOLD_ITALIC:
                this._fontSemiBoldItalic = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Semi_Bold_Italic);
                break;
            case FONT_LIGHT_ITALIC:
                this._fontLightItalic = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Light_Italic);
                break;
            case FONT_EXTRALIGHT_ITALIC:
                this._fontExtraLightItalic = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_ExtraLight_Italic);
                break;
            default:
                this._fontRegular = Typeface.createFromAsset(this.getAssets(), ASTConstants.FONT_Regular);
                break;

        }
    }

}
