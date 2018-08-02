package com.apitechnosoft.ipad.utils;

import android.graphics.Color;

/**
 * @author AST Inc.
 */
public interface ASTConstants {

	String notifSharedPrefName = "Notification";
	String BUSI_DATE_KEY = "busiDate";//"busiDate"
	String HDR_TXT_KEY = "headerTxt";
	String langIdKey = "langId";
	String defaultLangId = "en";
	String EXTRA_LOGIN_USER = "loggedinUer";

	String countryCodeKey = "countryCode";
	String defaultCountryCode = "US";
	String canadaCountryCode = "CA";

	String defaultCurrencyCode = "$";

	String DEVICE_TOKEN_LBL = "deviceToken";
	String ICON_FILE = "fonts/fontawesome-webfont.ttf";
	String FONT_Regular = "fonts/SourceSansPro-Regular.ttf";
	String FONT_Bold = "fonts/SourceSansPro-Bold.ttf";
	String FONT_Italic = "fonts/SourceSansPro-Italic.ttf";
	String FONT_Bold_Italic = "fonts/SourceSansPro-BoldItalic.ttf";
	String FONT_Semi_Bold = "fonts/SourceSansPro-Semibold.ttf";
	String FONT_Semi_Bold_Italic = "fonts/SourceSansPro-SemiboldItalic.ttf";
	String FONT_Light_Italic = "fonts/SourceSansPro-LightItalic.ttf";
	String FONT_ExtraLight_Italic = "fonts/SourceSansPro-ExtraLightItalic.ttf";
	String SSO_OBJ_LBL = "ssoObj";
	String SSO_CONFIG_FILE = "ssoconfig.json";
	String DEAFULT_TZ_STRING = "UTC";

	int ACTION_READ_CONTACTS = 11;
	int ACTION_CALL_PHONE = 12;
	int ACTION_CAPTURE_IMAGE = 13;

	String PAGE_LOAD = "PAGE_LOAD";
	String SYSTEM = "System";
	String SEARCH_RESULT = "SEARCH_RESULT";
	String MENU_ID = "menu_id";
	String DATE_TIME_FORMAT_WITH_ZONE = "MM/dd/yyyy HH:mm z";
	String phoneFormat = "XXX-XXX-XXXX";
	int maxHitsToOpenUrlDlg = 11;
	int lightBlackColor = Color.rgb(128, 128, 128);
	int WhiteColor = Color.rgb(255, 255, 255);
	String defaultRowFromat = "EEE MMM dd";
	String mailFormat = "MMM dd";
	String dateSelectionFormat = "MMM dd ''yy";
	String MONTH_YEAR = "MMMM ''yy";
	int MAX_LENGTH_PHONE = 10;
	int MIN_LENGTH_PHONE = 10;

	int ACTIVITY_CROP_REQUEST = 298;

	int NETWORK_UNAVAILABLE = -1;
	int CONNECTION_EXCEPTION = -2;

	String PREF_WRITE_EXTERNAL_STORAGE_REQUESTED = "writeExternalRequested";
	String PREF_CAMERA_REQUESTED = "cameraRequested";
	String PREF_WRITE_CALENDAR_REQUESTED = "writeCalendarRequested";
	String PREF_READ_CONTACTS_REQUESTED = "readContactsRequested";
	String PREF_ACCESS_FINE_LOCATION_REQUESTED = "fineLocationRequested";
	String PREF_ACCESS_COARSE_LOCATION_REQUESTED = "coarseLocationRequested";
	String PREF_READ_PHONE_STATE_REQUESTED = "readPhoneStateRequested";
	String PREF_RECORD_AUDIO_REQUESTED = "recordAudioRequested";
	String PREF_CALL_PHONE_REQUESTED = "callPhoneRequested";
	String SPECIAL_CHAR_REGEX = "^[éôÉèàêç.,'`~-]*$";
}
