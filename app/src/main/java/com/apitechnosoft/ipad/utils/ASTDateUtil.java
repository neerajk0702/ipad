package com.apitechnosoft.ipad.utils;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import com.apitechnosoft.ipad.ASTDate;

import static com.apitechnosoft.ipad.ASTDate.currentDate;

/**
 * @author AST Inc.
 */
public class ASTDateUtil {

	private static DateTimeFormatter dateFormatter;
	private static HashMap<String, String> tzMap;
	private static SimpleDateFormat intgDateFormatter;
	public static SimpleDateFormat customerDateFormatter;
	private static SimpleDateFormat headerDateFormatter;

	/*public static DateTimeFormatter dateFormatter() {
		if (dateFormatter == null) {
			dateFormatter = DateTimeFormat.forPattern(ApplicationHelper.application().intgDateFormat());
		}
		return dateFormatter;
	}*/

	public static String formatedDate(ASTDate date, String dateFormat) {
		return date == null ? null : formatedDate(date.toSqlDate(), dateFormat);
	}

	public static String formatedDate(java.sql.Date date, String dateFormat) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}

	/*public static ASTDate getDate(String _value) {
		return new ASTDate(dateFormatter().parseDateTime(_value));
	}*/

	public static ASTDate getDate(String _value, String dateFormat) {
		return new ASTDate(DateTimeFormat.forPattern(dateFormat).parseDateTime(_value));
	}

	/*public static Date getUtilDate(String _value) {
		try {
			return serverDateFormatter().parse(_value);
		} catch (ParseException e) {
			FNExceptionUtil.logException(e);
			return null;
		}
	}*/

	/*public static SimpleDateFormat serverDateFormatter() {
		if (intgDateFormatter == null) {
			intgDateFormatter = new SimpleDateFormat(ApplicationHelper.application().intgDateFormat());
		}
		return intgDateFormatter;
	}
*/
	/*public static SimpleDateFormat customerDateFormatter() {
		if (customerDateFormatter == null) {
			customerDateFormatter = new SimpleDateFormat(ApplicationHelper.application().loggedInUserDateFormat());
		}
		return customerDateFormatter;
	}*/

	public static SimpleDateFormat defaultRowFormatter() {
		return new SimpleDateFormat(ASTConstants.defaultRowFromat);
	}

	public static SimpleDateFormat dateSelectionFormat() {
		return new SimpleDateFormat(ASTConstants.dateSelectionFormat);
	}

	public static SimpleDateFormat monthYearFormat() {
		return new SimpleDateFormat(ASTConstants.MONTH_YEAR);
	}

	public static SimpleDateFormat mailFormatter() {
		return new SimpleDateFormat(ASTConstants.mailFormat);
	}

	public static SimpleDateFormat headerDateFormatter() {
		if (headerDateFormatter == null) {
			//headerDateFormatter = new SimpleDateFormat(ApplicationHelper.application().headerDateFormat());
		}
		return headerDateFormatter;
	}
/*
	public static ASTDate currentDate() {
		return FNClockTask.getInstance().currentDate();
	}*/

	public static ASTDate currentWeek() {
		return currentDate().startOfWeek();
	}

	public static ASTDate dateForIID(ASTEnum dateTypeIID) {
		return dateForIID(currentDate(), dateTypeIID);
	}

	public static ASTDate dateForIID(ASTDate date, ASTEnum dateTypeIID) {
		switch (dateTypeIID) {
			case MONTHLY:
				return date.startOfMonth();
			case WEEKLY:
			case TWO_WEEK:
				return date.startOfWeek();
			default:
				return date;
		}
	}

	public static String timeZoneId(String tzId) {
		if (tzMap == null) {
			tzMap = new HashMap<>();
			//	tzMap.put("America/Anchorage", "America/Halifax");
			tzMap.put("America/Halifax", "America/Puerto_Rico");
			tzMap.put("PST", "PST8PDT");
			tzMap.put("IST", "Asia/Calcutta");
			tzMap.put("CST", "CST6CDT");
			tzMap.put("AST", "America/Anguilla"); //AST  GMT-4:00
			tzMap.put("SST", "Pacific/Samoa"); // somoa Standerd time     GMT-11:00
		}
		String zoneId = tzMap.get(tzId);
		return zoneId != null ? zoneId : tzId;
	}



	public static String dayNameByWeekNumber(int dayOfWeek) {
		DateFormatSymbols dfs = DateFormatSymbols.getInstance(Locale.getDefault());
		return dfs.getWeekdays()[dayOfWeek % 7 + 1];
	}

	public static String monthNameByNumber(int dayOfMonths) {
		DateFormatSymbols dfs = DateFormatSymbols.getInstance(Locale.getDefault());
		return dfs.getMonths()[dayOfMonths % 12 + 1];
	}

	public static void reset() {
		headerDateFormatter = null;
		dateFormatter = null;
		customerDateFormatter = null;
		intgDateFormatter = null;
	}
}
