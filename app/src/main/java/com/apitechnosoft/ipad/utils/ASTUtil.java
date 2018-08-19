package com.apitechnosoft.ipad.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.apitechnosoft.ipad.FNSortOrdering;
import com.apitechnosoft.ipad.FNSortOrderingUtil;

import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.exception.FNExceptionUtil;
import com.apitechnosoft.ipad.filepicker.FNFilePicker;
import com.apitechnosoft.ipad.listener.PermissionRationaleDialogListener;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.apitechnosoft.ipad.filepicker.FNFilePicker.MEDIA_TYPE_DOCUMENT;

/**
 * @author AST Inc.
 */
public class ASTUtil {

    public static boolean isAlpha(String name) {
        return ASTObjectUtil.isNonEmptyStr(name) && name.matches("[a-zA-Z]+");
    }

    /**
     * @param emailId
     * @return true if emailID is in correct format
     */
    public static boolean isValidEmail(String emailId) {
        return ASTObjectUtil.isNonEmptyStr(emailId) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
    }

    public static String encodeObjectTobase64(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Bitmap) {
            return encodeTobase64((Bitmap) obj);
        } else if (obj instanceof File) {
            return encodeFileTobase64((File) obj);
        }
        return obj.toString();
    }

    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeFileTobase64(File file) {
        try {
            return Base64.encodeToString(FileUtils.readFileToByteArray(file), Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static String decodeStringFromBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
        return new String(decodedByte);
    }


    public static int getDipFromPixel(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            FNExceptionUtil.logException(e);
            return false;
        }
    }

    public static String stripSeparators(String phoneNumber) {
        String returnStr = "";
        if (ASTObjectUtil.isEmptyStr(phoneNumber)) {
            return returnStr;
        }
        for (Character c : phoneNumber.toCharArray()) {
            if (Character.isDigit(c) || c.toString().equals("+")) {
                returnStr += c;
            }
        }
        return returnStr;
    }


    public static String appNameWithoutSpace(Context context) {
        return appName(context).replaceAll(" ", "");
    }

    public static String appName(Context context) {
        return context.getString(R.string.app_name);
    }

    public static String to2Decimal(double value, double divider) {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(value / divider);
    }

    public static String to2Decimal(double value) {
        return to2Decimal(value, 100.0);
    }

    /**
     * Sets String resource to the given {@link TextView}. If the resource could
     * not be resolved with the given name, It sets the given resourceName as
     * text to the {@code TextView}
     *
     * @param resourceName        [{@code String}]
     * @param destinationTextView [{@code TextView}]
     */
    public static void setTextFromResourceName(String resourceName, TextView destinationTextView) {
        setTextFromResourceName(resourceName, destinationTextView, resourceName);
    }

    /**
     * Sets String resource to the given {@link TextView}. If the resource could
     * not be resolved with the given name, It sets the given optionalString to
     * the {@code TextView}
     *
     * @param resourceName        [{@code String}]
     * @param destinationTextView [{@code TextView}]
     * @param optionalString      - Optional String to be set on {@code TextView} in case of
     *                            wrong / unavailable resource name. Can be null.
     */
    public static void setTextFromResourceName(String resourceName, TextView destinationTextView, String optionalString) {
        int resoureId = ASTStringUtil.getIdForName(resourceName);
        if (resoureId != 0) {
            destinationTextView.setText(resoureId);
        } else {
            destinationTextView.setText(optionalString);
        }
    }

    public static String unicodeToString(String unicodeString) {
        return StringEscapeUtils.unescapeXml(unicodeString);
    }


    public static Long stringToNumber(String numberStr) {
        try {
            return Long.parseLong(ASTObjectUtil.isNonEmptyStr(numberStr) ? numberStr : "0");
        } catch (Exception e1) {
            return 0L;
        }
    }

    public static Float stringToFloat(String numberStr) {
        try {
            return Float.parseFloat(ASTObjectUtil.isNonEmptyStr(numberStr) ? numberStr : "0");
        } catch (Exception e1) {
            return 0f;
        }
    }

    public static Double stringToDouble(String numberStr) {
        try {
            return Double.parseDouble(ASTObjectUtil.isNonEmptyStr(numberStr) ? numberStr : "0");
        } catch (Exception e1) {
            return 0.0;
        }
    }


    public static void doCall(Context context, long phoneNumber) {
        doCall(context, String.valueOf(phoneNumber));
    }

    @SuppressWarnings("MissingPermission")
    public static void doCall(Context context, String phoneNumber) {
        if (ASTObjectUtil.isNonEmptyStr(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
        }
    }

    public static String capitalize(String s) {
        if (ASTObjectUtil.isEmptyStr(s)) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String stringForLengthAndChar(int length, char defaultChar) {
        String resultString = "";
        for (int i = 0; i < length; i++) {
            resultString += defaultChar;
        }
        return resultString;
    }

    public static int calculatePercentage(float value, float total) {
        return (int) calculatePercentage(value, total, 0);
    }

    public static float calculatePercentage(float value, float total, int scale) {
        float result = total != 0 ? (value / total) * 100 : 0;
        return result > 0 ? BigDecimal.valueOf(result).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue() : 0;
    }


    public static boolean isParsableNumber(String aValue) {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        try {
            format.parse(aValue).floatValue();
            return true;
        } catch (ParseException e) {
            Float.parseFloat(aValue);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }


    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isAboveLollipop()
    {
        boolean isAboveHOneyComb = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            isAboveHOneyComb = true;
        }
        return isAboveHOneyComb;
    }




    public static boolean isCollectionEmpty(Collection arrayList)
    {
        if(arrayList != null && arrayList.size() > 0)
        {
            return false;
        }
        return true;
    }

    /**
     * Sort list containing object on the basis of key.
     *
     * @param array
     * @param key
     * @param isAsc
     */
    public static <T> void sortArray(List<T> array, String key, boolean isAsc) {
        FNSortOrdering[] sortOrderArray = FNSortOrderingUtil.create(key, isAsc ? "ASC" : "DESC");
        FNSortOrderingUtil.sort(array, sortOrderArray);
    }


    public static void startFilePicker(Activity activity, int limit, long sizeLimit) {
        FNFilePicker.options(activity)
                .addMedia(MEDIA_TYPE_IMAGE)
                .addMedia(MEDIA_TYPE_VIDEO)
                .addMedia(MEDIA_TYPE_AUDIO)
                .addMedia(MEDIA_TYPE_DOCUMENT)
                .returnAfterFirst(false)
                .limit(limit)
                .sizeLimit(sizeLimit)
                .single()
                .imageDirectory(appNameWithoutSpace(activity)) // captured image directory name ("Camera" folder by default)
                .start(FNReqResCode.ATTACHMENT_REQUEST); // start image picker activity with request code

    }

}
