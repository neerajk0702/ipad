package com.apitechnosoft.ipad.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.activity.MainActivity;
import com.apitechnosoft.ipad.component.ASTFontViewField;
import com.apitechnosoft.ipad.component.ASTTextView;
import com.apitechnosoft.ipad.constants.Contants;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.listener.AlertDialogBoxClickInterface;
import com.google.gson.internal.Primitives;

import com.apitechnosoft.ipad.ApplicationHelper;

import com.apitechnosoft.ipad.component.ASTAlertDialog;
import com.apitechnosoft.ipad.component.ASTErrorIndicator;
import com.apitechnosoft.ipad.resource.FNResources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.apitechnosoft.ipad.utils.ASTUtil.appNameWithoutSpace;

/**
 * @author AST Inc.
 */
public class ASTUIUtil {

    private static volatile ASTErrorIndicator popupWindowDialog;

    public static void showDialog(Context context, String msg) {
        ASTAlertDialog dialog = new ASTAlertDialog(context);
        dialog.show(msg);
    }

    public static void showDialog(Context context, @StringRes int msgID) {
        ASTAlertDialog dialog = new ASTAlertDialog(context);
        dialog.show(msgID);
    }

    public static void showToast(String msg) {
        showToast(ApplicationHelper.application().getActivity(), msg);
    }

    public static void showToast(Context context, String msg) {
        final Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1500);
    }


    public static void shownewErrorIndicator(Context context, String errorMsg) {
        popupWindowDialog = new ASTErrorIndicator(context);
        popupWindowDialog.show(ApplicationHelper.application().getActivity().findViewById(R.id.headerFragment), errorMsg);
    }

    public static void showErrorIndicator(String errorMsg) {
        showErrorIndicator(ApplicationHelper.application().getContext(), errorMsg);
    }

    public static void showErrorIndicator(Context context, String errorMsg) {
        if (ApplicationHelper.application().getActivity() != null && ApplicationHelper.application().getActivity() instanceof MainActivity) {
            showErrorIndicator(context, errorMsg, ApplicationHelper.application().getActivity().findViewById(R.id.headerFragment));
        }
    }


    public static void showErrorIndicator(Context context, String errorMsg, View belowOf) {
        if (ApplicationHelper.application().getActivity() != null && ApplicationHelper.application().getActivity().findViewById(R.id.errorIndicatorView) != null) {
            final LinearLayout errorIndicatorView;
            errorIndicatorView = ApplicationHelper.application().getActivity().findViewById(R.id.errorIndicatorView);
            ASTTextView errorMsgView = ApplicationHelper.application().getActivity().findViewById(R.id.error_msg);
            ASTFontViewField closeButton = ApplicationHelper.application().getActivity().findViewById(R.id.closeErrorIndicator);
            if (errorIndicatorView.getVisibility() == View.VISIBLE) {
                return;
            }
            errorIndicatorView.setVisibility(View.VISIBLE);
            errorMsgView.setText(errorMsg);
            resizingView(errorIndicatorView, 150, -2, false);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resizingView(errorIndicatorView, 150, 0, true);
                }
            }, 3000);


            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resizingView(errorIndicatorView, 150, 0, true);
                }
            });

        } else {
            if (belowOf == null || (popupWindowDialog != null && popupWindowDialog.isShowing())) {
                return;
            }
            popupWindowDialog = new ASTErrorIndicator(context);
            popupWindowDialog.show(belowOf, errorMsg);
        }
    }


    public static void resizingView(final View v, int duration, int targetHeight, final boolean isCollapse) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(isCollapse ? v.getMeasuredHeight() : 0, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                if (-2.0 < height && height < -0.99) {
                    return;
                }
                v.getLayoutParams().height = height;
                v.requestLayout();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isCollapse) {
                    v.setVisibility(View.GONE);
                }
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void hideKeyBoard(Context context, View v) {
        if (v == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        hideKeyBoard(activity, activity.getCurrentFocus());
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            hideKeyBoard(activity, activity.getCurrentFocus());
        } else {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
    }

    public static void hideKeyBoardFromDialog(Context context, Dialog view) {
        hideKeyBoard(context, view.getCurrentFocus());
    }

    public static Animation animBlink() {
        return AnimationUtils.loadAnimation(ApplicationHelper.application().getActivity(), R.anim.blink_anim);
    }

    public static float dimenInDip(int size, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
    }

    public static void cancelAnimation(TextView view) {
        if (view == null || view.getAnimation() == null) {
            return;
        }
        view.getAnimation().cancel();
        view.clearAnimation();
        view.setAnimation(null);
    }

    public static void startAnimation(TextView view) {
        if (view == null || view.getAnimation() != null) {
            return;
        }
        view.startAnimation(animBlink());
    }

    public static int headerColor() {
        return R.color.colorPrimary;
    }

    public static void tintWidget(Context context, View view, int colorResId) {
        Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
        DrawableCompat.setTint(wrappedDrawable, getColor(context, colorResId));
        setBackground(wrappedDrawable, view);
    }

    public static Drawable getDrawable(@DrawableRes int drawableResId) {
        return ContextCompat.getDrawable(ApplicationHelper.application().getBaseContext(), drawableResId);
    }

    public static Drawable getDrawable(String drawableResName) {
        return getDrawable(FNResources.drawable.get(drawableResName));
    }

    public static int getColor(Context context, @ColorRes int colorId) {
        try {
            return ContextCompat.getColor(context, colorId);
        } catch (Exception e) {
            return colorId;
        }
    }

    public static int getColor(@ColorRes int colorId) {
        return getColor(ApplicationHelper.application().getBaseContext(), colorId);
    }

    public static <T> T findViewById(View view, Class<T> classOf, int resID) {
        if (view == null || resID <= 0) {
            return null;
        }
        try {
            View v = view.findViewById(resID);
            return Primitives.wrap(classOf).cast(v);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T findViewByName(View view, Class<T> classOf, String resName) {
        return findViewById(view, classOf, FNResources.id.get(resName));
    }

    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int) Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    /**
     * Sets Background {@link Drawable} to any given view. Checks for the SDK
     * version and calls the appropriate method. This method is made to handle
     * the deprecation issue of setBackgroudDrawable method.
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(Drawable drawable, View view) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundOld(view, drawable);
        } else {
            setBackgroundNew(view, drawable);
        }
    }

    public static void setBackground(View v) {
        setBackground(null, v);
    }

    public static void setBackground(@DrawableRes int resId, View v) {
        setBackground(getDrawable(resId), v);
    }

    @SuppressWarnings("deprecation")
    private static void setBackgroundOld(View view, Drawable drawable) {
        view.setBackgroundDrawable(drawable);
    }

    @SuppressLint("NewApi")
    private static void setBackgroundNew(View view, Drawable drawable) {
        view.setBackground(drawable);
    }

    public static StateListDrawable getStateListDrawable(@ColorRes int pressedColor, @ColorRes int disableStateColor, @ColorRes int enableStateColor, float radius) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                getRectShape(getColor(pressedColor), radius));
        states.addState(new int[]{-android.R.attr.state_enabled},
                getRectShape(getColor(disableStateColor), radius));
        states.addState(new int[]{android.R.attr.state_enabled},
                getRectShape(getColor(enableStateColor), radius));
        return states;
    }

    public static void setBackground(View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth, float[] radii, ASTEnum shape) {
        GradientDrawable drawable = getShape(getColor(bgColor), getColor(strokeColor), strokeWidth, radii, shape);
        setBackground(drawable, v);
    }

    public static void setBackgroundRect(View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth, float radius) {
        setBackground(v, bgColor, strokeColor, strokeWidth, new float[]{radius}, ASTEnum.RECT_SHAPE);
    }

    public static void setBackgroundRect(View v, @ColorRes int bgColor, float radius) {
        GradientDrawable drawable = getRectShape(getColor(bgColor), radius);
        setBackground(drawable, v);
    }

    public static void setBackgroundRound(View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth, float[] radii) {
        setBackground(v, bgColor, strokeColor, strokeWidth, radii, ASTEnum.ROUND_CORNER);
    }

    public static void setBackgroundRound(View v, @ColorRes int bgColor, float[] radii) {
        GradientDrawable drawable = getRoundCorner(getColor(bgColor), radii);
        setBackground(drawable, v);
    }

    public static void setBackgroundOval(View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth) {
        GradientDrawable drawable = getOvalShape(getColor(bgColor), getColor(strokeColor), strokeWidth);
        setBackground(drawable, v);
    }

    public static void setBackgroundOval(View v, @ColorInt int bgColor, @ColorInt int strokeColor) {
        GradientDrawable drawable = getOvalShape(bgColor, strokeColor, 0);
        setBackground(drawable, v);
    }

    public static void setBackgroundOval(View v, @ColorRes int bgColor) {
        setBackgroundOval(v, bgColor, bgColor, 0);
    }

    public static void setBackgroundRing(View v, @ColorRes int bgColor, @ColorRes int strokeColor, int strokeWidth) {
        GradientDrawable drawable = getRingShape(getColor(bgColor), getColor(strokeColor), strokeWidth);
        setBackground(drawable, v);
    }

    public static void setBackgroundRing(View v, @ColorInt int bgColor, @ColorInt int strokeColor) {
        GradientDrawable drawable = getRingShape(bgColor, strokeColor, 0);
        setBackground(drawable, v);
    }

    public static void setBackgroundRing(View v, @ColorRes int bgColor) {
        setBackgroundRing(v, bgColor, bgColor, 0);
    }

    public static StateListDrawable getSelector(Context context, String image) {
        StateListDrawable states = new StateListDrawable();
        Drawable pressedImage = getDrawable(image + "_press");
        states.addState(new int[]{android.R.attr.state_pressed}, pressedImage);
        states.addState(new int[]{android.R.attr.state_focused}, pressedImage);
        states.addState(new int[]{android.R.attr.state_checked}, pressedImage);
        states.addState(new int[]{android.R.attr.state_selected}, pressedImage);
        states.addState(new int[]{}, getDrawable(image + "_default"));
        return states;
    }

    public static GradientDrawable getShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth, float[] radii, ASTEnum shape) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{});
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(bgColor);
        switch (shape) {
            case RECT_SHAPE:
                gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                float radius = radii[0];
                gradientDrawable.setCornerRadius(radius);
                break;
            case ROUND_CORNER:
                gradientDrawable.setCornerRadii(radii);
                break;
            case OVAL_SHAPE:
                gradientDrawable.setShape(GradientDrawable.OVAL);
                break;
            case RING_SHAPE:
                gradientDrawable.setShape(GradientDrawable.RING);
                break;
        }
        return gradientDrawable;
    }

    public static GradientDrawable getRectShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth, float radius) {
        return getShape(bgColor, strokeColor, strokeWidth, new float[]{radius}, ASTEnum.RECT_SHAPE);
    }

    public static GradientDrawable getRectShape(@ColorInt int bgColor, float radius) {
        return getShape(bgColor, Color.TRANSPARENT, 2, new float[]{radius}, ASTEnum.RECT_SHAPE);
    }

    public static GradientDrawable getRoundCorner(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth, float[] radii) {
        return getShape(bgColor, strokeColor, strokeWidth, radii, ASTEnum.ROUND_CORNER);
    }

    public static GradientDrawable getRoundCorner(@ColorInt int bgColor, float[] radii) {
        return getShape(bgColor, Color.TRANSPARENT, 0, radii, ASTEnum.ROUND_CORNER);
    }

    public static GradientDrawable getOvalShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth) {
        return getShape(bgColor, strokeColor, strokeWidth, new float[]{0}, ASTEnum.OVAL_SHAPE);
    }

    public static GradientDrawable getOvalShape(@ColorInt int bgColor) {
        return getShape(bgColor, Color.TRANSPARENT, 0, new float[]{0}, ASTEnum.OVAL_SHAPE);
    }

    public static GradientDrawable getRingShape(@ColorInt int bgColor, @ColorInt int strokeColor, int strokeWidth) {
        return getShape(bgColor, strokeColor, strokeWidth, new float[]{0}, ASTEnum.RING_SHAPE);
    }

    public static GradientDrawable getRingShape(@ColorInt int bgColor) {
        return getShape(bgColor, Color.TRANSPARENT, 0, new float[]{0}, ASTEnum.RING_SHAPE);
    }

    public static int getHeight(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getConfiguration().screenWidthDp;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getConfiguration().screenHeightDp;
    }

    public static void enableTouch() {
        if (getWindow() == null) {
            return;
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void disableTouch() {
        if (getWindow() == null) {
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private static Window getWindow() {
        MainActivity activity = ApplicationHelper.application().getActivity();
        if (activity == null)
            return null;
        return activity.getWindow();
    }

    public static void showKeyBoard(Activity context, View view) {
        if (view == null) {
            return;
        }
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void setFontTypeFace(TextView v, int fontStyle) {
        switch (fontStyle) {
            case Typeface.BOLD:
                setFontTypeFace(v, ASTEnum.FONT_BOLD);
                break;
            case Typeface.BOLD_ITALIC:
                setFontTypeFace(v, ASTEnum.FONT_BOLD_ITALIC);
                break;
            case Typeface.ITALIC:
                setFontTypeFace(v, ASTEnum.FONT_ITALIC);
                break;
            default:
                setFontTypeFace(v, ASTEnum.FONT_REGULAR);
                break;
        }
    }

    public static void setFontTypeFace(TextView v, ASTEnum fontTypeFace) {
        try {
            v.setTypeface(ApplicationHelper.application().getFontTypeFace(fontTypeFace));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static float getDimension(@DimenRes int dimenId) {
        return ApplicationHelper.application().getResources().getDimension(dimenId);
    }

    public static int getDimensionInt(@DimenRes int dimenId) {
        return (int) getDimension(dimenId);
    }


    public static void showCustomConfirmDialog(Activity context, String msg, int imageResource, String positiveBtnCaption, String negativeBtnCaption,
                                               final AlertDialogBoxClickInterface alertDialogBoxClickListener) {
        TextView textViewMsg;
        ImageView imageView;
        TextView buttonPositive;
        TextView buttonNegative;
        AlertDialog alertDialog;
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            ViewGroup parent = null;
            View dialogView = inflater.inflate(R.layout.confirmation_dialog_layout, parent);

            textViewMsg = (TextView) dialogView.findViewById(R.id.textViewMessage);
            textViewMsg.setText(msg);

            imageView = (ImageView) dialogView.findViewById(R.id.imageViewAlertImage);
            if (imageResource == -1) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setBackgroundResource(imageResource);
            }

            CustomConfirmAlertButtonClickListener customConfirmAlertButtonClickListener = new CustomConfirmAlertButtonClickListener(alertDialog, alertDialogBoxClickListener);

            buttonPositive = (TextView) dialogView.findViewById(R.id.buttonPositive);
            buttonPositive.setText(positiveBtnCaption);
            buttonPositive.setOnClickListener(customConfirmAlertButtonClickListener);

            buttonNegative = (TextView) dialogView.findViewById(R.id.buttonNegative);
            buttonNegative.setText(negativeBtnCaption);
            buttonNegative.setOnClickListener(customConfirmAlertButtonClickListener);

            alertDialog.show();
            alertDialog.setContentView(dialogView);
        } catch (Exception exception) {

        }
    }

    private static class CustomConfirmAlertButtonClickListener implements View.OnClickListener {
        private AlertDialogBoxClickInterface alertDialogClickListener;
        private AlertDialog alertDialog;

        public CustomConfirmAlertButtonClickListener(AlertDialog alertDialog, AlertDialogBoxClickInterface alertDialogClickListener) {
            this.alertDialogClickListener = alertDialogClickListener;
            this.alertDialog = alertDialog;
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            boolean isPositiveButtonClicked;
            switch (id) {
                case R.id.buttonPositive:
                    isPositiveButtonClicked = true;
                    alertDialogClickListener.onButtonClicked(isPositiveButtonClicked);
                    break;
                case R.id.buttonNegative:
                    isPositiveButtonClicked = false;
                    alertDialogClickListener.onButtonClicked(isPositiveButtonClicked);
                    break;
                default:
                    break;
            }

            dismissDialog(alertDialog);
        }
    }

    public static void dismissDialog(Dialog dialog) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception exception) {
            // Consume it
        }
    }

    public static void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public static void hideViewByInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    public static void showToastNotification(Context context, String message, String title, boolean isLongDuration) {
        int duration = 0;

        if (isLongDuration) {
            duration = Toast.LENGTH_LONG;
        } else {
            duration = Toast.LENGTH_SHORT;
        }

        if (message != null && !message.isEmpty()) {
            if (title != null) {
                String toastMessage = title + "\n\n" + message;
                Toast.makeText(context, toastMessage, duration).show();
            } else {
                String toastMessage = message;
                Toast.makeText(context, toastMessage, duration).show();
            }
        }
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        int version = 1;
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);
        }
        return version;
    }

    //get app version name
    public static String getAppVersionName(Context context) {
        String version = "";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);

        }
        return version;
    }

    //get app package name
    public static String getAppPackageName(Context context) {
        String pkName = "";
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            pkName = packageInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            // throw new RuntimeException("Could not get package name: " + e);

        }
        return pkName;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return (model);
        } else {
            return (manufacturer) + " " + model;
        }
    }

    public static Date parseDateFromString(String strDate) {
        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = format.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toCalendar(final String iso8601string) throws ParseException {
        //GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        String s = iso8601string;
        Date date = null;
        try {
            s = s.replace("Z", "+00:00");
            try {
                s = s.substring(0, 26) + s.substring(27);
            } catch (IndexOutOfBoundsException e) {
            }
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(s);
        } catch (Exception tc) {
            s = s.replace("Z", "+00:00");
            try {
                s = s.substring(0, 22) + s.substring(23);
            } catch (IndexOutOfBoundsException e) {
            }
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s);
        }
        //calendar.setTime(date);
        return date;
    }

    //convert date for get day month year hours min am/pm
    public static String convertDate(Date date) {
        StringBuilder sb = new StringBuilder();
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        String hours = (String) android.text.format.DateFormat.format("h", date); //20
        String min = (String) android.text.format.DateFormat.format("mm", date); //20
        String ampm = (String) android.text.format.DateFormat.format("aa", date); //20
        //Log.d(Contants.LOG_TAG, "dayOfTheWeek*********" + dayOfTheWeek);
        //Log.d(Contants.LOG_TAG, "year*********" + year);
        //Log.d(Contants.LOG_TAG, "stringMonth*********" + stringMonth+"  "+intMonth);
           /* Log.d(Contants.LOG_TAG, "day*********" + day);
            Log.d(Contants.LOG_TAG, "hours*********" + hours);
            Log.d(Contants.LOG_TAG, "min*********" + min);
            Log.d(Contants.LOG_TAG, "ampm*********" + ampm);*/
        List<String> list = new ArrayList<String>();
        list.add(dayOfTheWeek);
        list.add(day);
        list.add(stringMonth);
        list.add(hours);
        list.add(min);
        list.add(ampm);
        list.add(year);
        list.add(intMonth);
        String delim = "";
        for (String i : list) {
            sb.append(delim).append(i);
            delim = ",";
        }
        return sb.toString();
    }


    //calculate total days between to days
    public static int daysBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    //encode image into base64 string
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        if (image != null) {
            image.compress(compressFormat, quality, byteArrayOS);
        }
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    //decode base64 string into image
    public static Bitmap decodeBase64(String input) {
        Bitmap bitmap = null;
        if (input != null) {
            byte[] decodedBytes = Base64.decode(input, 0);
            bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
        return bitmap;
    }

    //check online
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager;
        boolean connected = false;
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            //System.out.println("CheckConnectivity Exception: " + e.getMessage());
            //Log.v("connectivity", e.toString());
        }
        return connected;
    }

    //alert for error message
    public static void alertForErrorMessage(String errorMessage, Context mContext) {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        Typeface roboto_regular = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto.regular.ttf");
        final android.app.AlertDialog alert = builder.create();
        // alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.custom_error_alert, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(roboto_regular);
        TextView ok = (TextView) view.findViewById(R.id.Ok);
        ok.setTypeface(roboto_regular);
        title.setText(errorMessage);
        alert.setCustomTitle(view);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    public static void alertNative(Context mContext) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
        AlertDialog dialog = builder.create();
        //dialog.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        dialog.setMessage("hello");
        dialog.setTitle("hi");
        dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#ffffff"));
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#ffffff"));
    }

    //set medicine id
    public static void setMedicineId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("MedicineIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("medicineId", id);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //set State id
    public static void setStateId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("StateIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("StateId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set City id
    public static void setCityId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("CityIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("CityId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set State id for add new Address
    public static void setAddNewAddressStateId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AddNewAddressStatePreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("StateId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set City id for add new Address
    public static void setAddNewAddressCityId(Context context, int id) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AddNewAddressCityPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("CityId", id);
            editor.putBoolean("SelectFlage", true);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set device id for  GCM
    public static void setDeviceIDIntoSharedPreferences(Context context, String device_token) {
        SharedPreferences prefs = context.getSharedPreferences("GCMDeviceId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("device_token", device_token);
        if (Contants.IS_DEBUG_LOG) {
            Log.d(Contants.LOG_TAG, "device_token set*********" + device_token);
        }
        editor.commit();
    }

    //get device id for  GCM
    public static String getDeviceIDFromSharedPreferences(Context context) {
        String device_token = null;
        SharedPreferences prefs = context.getSharedPreferences("GCMDeviceId", Context.MODE_PRIVATE);
        if (prefs != null) {
            device_token = prefs.getString("device_token", null);
            if (Contants.IS_DEBUG_LOG) {
                Log.d(Contants.LOG_TAG, "device_token get*********" + device_token);
            }
        }
        return device_token;
    }


    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Uri uri = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        if (path != null) {
            uri = Uri.parse(path);
        }
        return uri;
    }

    //clear all bask stack fragment
    public static void clearBackStack(Context context) {
        //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }


 /*   //chack run time EXTERNAL_STORAGE permission
    public boolean checkExternalStoragePermission(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission Necessary");
                    alertBuilder.setMessage("External Storage Permission is Necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }*/


    //set Access Token for send with all service call
    public static void setAccessToken(Context context, String accessToken) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AccessTokenPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("AccessToken", accessToken);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set loginId for send with all service call
    public static void setLoginId(Context context, int loginId) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("LoginIdPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("LoginId", loginId);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }
     /* //call service through OkHttpClient
     private void doPostRequest(final String url, final String json) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("ontent-Type", "application/json")
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    Log.d(Contants.LOG_TAG, "Access response*****" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String responce) {
                super.onPostExecute(responce);
            }
        }.execute();
    }*/

    //get app tour run only first time or not
    public static Boolean getAppTourRunStatus(Context context) {
        Boolean tourFlag;
        try {
            SharedPreferences prefs = context.getSharedPreferences("AppTourPreferences", Context.MODE_PRIVATE);
            tourFlag = prefs.getBoolean("AppTourFlag", false);
            return tourFlag;
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception  - getLanguage" + e.getMessage());
        }
        return null;
    }

    //set app tour run only first time flag
    public static void setAppTourRunStatus(Boolean flag, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("AppTourPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("AppTourFlag", flag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //set firstime activity run only first time flag
    public static void setFirstTimeActivityStatus(Boolean flag, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("FirstTimeActivity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTimeFlag", flag);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //get app tour run only first time or not
    public static Boolean getFirstTimeActivityStatus(Context context) {
        Boolean tourFlag;
        try {
            SharedPreferences prefs = context.getSharedPreferences("FirstTimeActivity", Context.MODE_PRIVATE);
            tourFlag = prefs.getBoolean("FirstTimeFlag", false);
            return tourFlag;
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception  - getLanguage" + e.getMessage());
        }
        return null;
    }


    public static String getUserPhoneNo(Context context) {
        String phone;
        try {
            SharedPreferences prefs = context.getSharedPreferences("UserPhonePreferences", Context.MODE_PRIVATE);
            phone = prefs.getString("Phonenumber", "");
            return phone;
        } catch (Exception e) {
            Log.d(Contants.LOG_TAG, "Exception  - Phone" + e.getMessage());
        }
        return null;
    }

    public static void setUserPhoneNo(Context context, String phone) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("UserPhonePreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Phonenumber", phone);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }


    //------------------Get Wifi Connectivity--------------------------

    /*public String getInternetStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        return String.valueOf(linkSpeed);
    }*/

    //-----------------------------------------------------------------

    //------------------Check Network Connectivity------------------

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    //--------------------------------------------------------------

    public String formatDate(String dateInMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String dateString = "";
        if (!dateInMillis.contains("/") && !dateInMillis.equals("")) {
            dateString = formatter.format(new Date(Long.parseLong(dateInMillis)));
        } else {
            dateString = dateInMillis;
        }
        return dateString;
    }

    public void SendMessage(Context context, String contact, String messageBody) {

        try {
            /*Intent intent = new Intent(context.getApplicationContext(), PlannedActivityListActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);

            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(contact, null, messageBody, pi,null);*/

            /*Intent intent = new Intent(context.getApplicationContext(), CircleActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);*/
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact, null, messageBody, null, null);
            /*Toast.makeText(context.getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();*/
        } catch (Exception ex) {
            /*Toast.makeText(context.getApplicationContext(),
                    ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();*/
            ex.printStackTrace();
        }
    }


    public Location getLocation(Context context) {
        Location location;
        GPSTracker gpsTracker = new GPSTracker(context);
        location = gpsTracker.getLocation();

        if (location == null) {
            //Toast.makeText(context, "Location Not Available", Toast.LENGTH_SHORT).show();

        } else {
            /*lat = location.getLatitude();
            lon = location.getLongitude();*/
        }

        return location;
    }

    public String[] concatinateStringArray(String[] a, String[] b) {
        int length = a.length + b.length;
        String[] result = new String[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static String getLocationAddress(Location location, Context context, String addressType) throws IOException {
        String address = "";


        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.ENGLISH);

            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addressType.equals("short")) {
                address = addresses.get(0).getAddressLine(0) + "|   " +
                        addresses.get(0).getLocality();
            } else if (addressType.equals("locality")) {
                address = addresses.get(0).getLocality();
            } else {
                address = addresses.get(0).getAddressLine(0) + ", " +
                        addresses.get(0).getLocality() + ", " +
                        addresses.get(0).getAdminArea() + ", " +
                        addresses.get(0).getCountryName();
            }
        } catch (Exception ex) {
            address = "Unable to connect";
        }

        if (location.getLongitude() == 0.0000) {
            address = "Unable to connect";
        }
        return address;
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        if (!String.valueOf(lat1).equals("0.0000") || !String.valueOf(lat2).equals("0.0000")) {
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
        } else {
            dist = 0;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String getOsVersion() {
        /*String[] mapper = new String[]{
                "1.0", "1.1", "1.2", "2.0",
                "2.1", "2.1.1", "2.1.2", "2.2", "2.3",
                "3.0", "3.1", "3.2", "4.0.1",
                "4.0.4", "4.1", "4.2", "4.3", "4.4"
                , "5.0", "5.1", "6.0", "6.0.1", "7.0", "7.1"
        };*/

        int index = Build.VERSION.SDK_INT;
        //String versionName = mapper[index];

        //return versionName;
        return String.valueOf(index);
    }

    public void downloadapk(Context context, String apkName) {
        try {
            URL url = new URL("http://54.255.164.25:9050/android/" + apkName);
            //URL url = new URL("http://54.255.164.25:9050/android/ASTATMAppVer1.6.15.apk");
            //URL url = new URL("http://file.appsapk.com/downloads/2016/052016/chris.cooper.androids.full.apk");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            //urlConnection.setDoOutput(true);
            //urlConnection.disconnect();
            urlConnection.setRequestProperty("connection", "close");
            urlConnection.connect();

            File sdcard = Environment.getExternalStorageDirectory();
            //File file = new File(sdcard, "ASTATMAppVer1.6.15.apk");
            File file = new File(sdcard, apkName);
            //File file = new File(sdcard, "chris.cooper.androids.full.apk");

            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();

            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
            //this.checkUnknownSourceEnability();
            this.installApk(context, apkName);

        } catch (Exception ex) {
            //ex.printStackTrace();
            //Log.v("Exception", ex.getLocalizedMessage());
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */
    }

    private void installApk(Context context, String apkName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File("/sdcard/" + apkName));
            //Uri uri = Uri.fromFile(new File("/sdcard/ASTATMAppVer1.6.15.apk" ));
            //Uri uri = Uri.fromFile(new File("/sdcard/chris.cooper.androids.full.apk"));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);

            /*SharedPreferences pref;
            pref = context.getApplicationContext().getSharedPreferences("SharedPref", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            //editor.putString("appName", apkName);
            editor.putString("appName", apkName);
            editor.commit();*/
        } catch (Exception Ex) {
            String exception = Ex.getLocalizedMessage();
        }
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static boolean checkGpsEnabled(final Context context) {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayGPSDialog(context);
        } else {
            gpsEnabled = true;
        }
        return gpsEnabled;
    }

    public static void displayGPSDialog(final Context context) {
        AlertDialog.Builder gpsonBuilder = new AlertDialog.Builder(context);
        gpsonBuilder.setTitle("Please Enable your Location Services(GPS)");
        gpsonBuilder.setPositiveButton("ON", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        gpsonBuilder.show();
    }

    //save current date
    public static void saveCurrentDateToPre(String currentDate, Context context) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("CurrentDatePfe", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("CurrentDate", currentDate);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }

    //get current Date
    public static String getCurrentDateFromPre(Context context) {
        String cDate;
        try {
            SharedPreferences prefs = context.getSharedPreferences("CurrentDatePfe", Context.MODE_PRIVATE);
            cDate = prefs.getString("CurrentDate", "");
            return cDate;
        } catch (Exception e) {
            // Log.d(Contants.LOG_TAG, "Exception  - getLanguage" + e.getMessage());
        }
        return null;
    }

    /*public String getOsVersion(){
        String[] mapper = new String[] {
                "1.0", "1.1", "1.2", "2.0",
                "2.1", "2.1.1", "2.1.2", "2.2", "2.3",
                "3.0", "3.1", "3.2", "4.0.1",
                "4.0.4", "4.1", "4.2", "4.3", "4.4"
                , "5.0", "5.1", "6.0"
        };

        int index = Build.VERSION.SDK_INT - 2;
        String versionName = mapper[index];

        return
    }*/

    public static void startImagePicker(Activity activity) {
        startImagePicker(activity, true);
    }

    public static void startImagePicker(Activity activity, boolean isReturnAfterFirst) {
        FNFilePicker.options(activity)
                .addMedia(MEDIA_TYPE_IMAGE)
                .returnAfterFirst(isReturnAfterFirst) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .single() // multi mode (default mode)
                .imageDirectory(appNameWithoutSpace(activity)) // captured image directory name ("Camera" folder by default)
                .start(FNReqResCode.ATTACHMENT_REQUEST); // start image picker activity with request code
    }


    public String getFormattedDate(String dateFormat, long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    //rename file
    public static File renameFile(String imageFileName, String newImageName) {
        //File sdcardPath = new File(Environment.getExternalStorageDirectory(), Contants.APP_DIRECTORY);
        // sdcardPath.mkdirs();

        Context appContext = ApplicationHelper.application().getApplicationContext();
        File directory = new File(appContext.getCacheDir(), imageFileName);
        Log.d(Contants.LOG_TAG, "OLd file name and path __: " + directory.getPath());
        File newFileName = new File(appContext.getCacheDir(), newImageName);
        boolean success = directory.renameTo(newFileName);
        Log.d(Contants.LOG_TAG, "New file name and path __: " + newFileName.getPath() + "getName__" + newFileName.getName());
        return newFileName;
    }
    public static void setUserId(Context context, String id,String password,String fname,String profileimage) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserId", id);
            editor.putString("Password", password);
            editor.putString("FirstName", fname);
            editor.putString("profileimage", profileimage);
            editor.commit();
        } catch (Exception e) {
            // should never happen
            //   throw new RuntimeException("Could not get language: " + e);
        }
    }
}
