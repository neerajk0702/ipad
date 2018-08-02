package com.apitechnosoft.ipad;

import android.support.annotation.NonNull;

import com.apitechnosoft.ipad.utils.ASTDateUtil;
import com.apitechnosoft.ipad.utils.ASTEnum;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.sql.Date;
import java.util.Locale;

/**
 * @author AST Inc.
 */
public class ASTDate extends ASTObject implements Comparable<ASTDate> {

	public LocalDate dateMidNight;
	String customerFormat;
	String defaultRowFormat;
	String headerFormat;
	String serverFormat;
	String mailFormat;

	public ASTDate() {
		this.dateMidNight = new LocalDate();
	}

	public ASTDate(java.util.Date date) {
		this.dateMidNight = new LocalDate(date);
	}

	public ASTDate(LocalDate dateMidnight) {
		this.dateMidNight = dateMidnight;
	}

	public ASTDate(Object paramObject) {
		this.dateMidNight = new LocalDate(paramObject, (Chronology) null);
	}

	public ASTDate(DateTimeZone paramDateTimeZone) {
		this.dateMidNight = new LocalDate(paramDateTimeZone);
	}

	public ASTDate(int paramInt1, int paramInt2, int paramInt3) {
		this.dateMidNight = new LocalDate(paramInt1, paramInt2, paramInt3);
	}

	public static ASTDate currentDate() {
		return new ASTDate();
	}

	public static java.util.Date currentUtilDate() {
		return currentDate().toDate();
	}

	public Date toSqlDate() {
		return new Date(this.dateMidNight.toDate().getTime());
	}

	public ASTDate offSetDay(int offset) {
		return new ASTDate(this.dateMidNight.plusDays(offset));
	}

	public ASTDate offSetWeek(int offset) {
		return new ASTDate(this.dateMidNight.plusWeeks(offset));
	}

	public ASTDate offSetMonth(int offset) {
		return new ASTDate(this.dateMidNight.plusMonths(offset));
	}

	public ASTDate offSetYear(int offset) {
		return new ASTDate(this.dateMidNight.plusYears(offset));
	}

	public void setDateMidNight(LocalDate dateMidNight) {
		this.dateMidNight = dateMidNight;
	}

	public java.util.Date toDate() {
		return this.dateMidNight.toDate();
	}

	public ASTDate startOfMonth() {
		return new ASTDate(this.dateMidNight.getYear(), this.dateMidNight.getMonthOfYear(), this.dateMidNight.dayOfMonth().getMinimumValue());
	}

	public ASTDate endOfMonth() {
		return new ASTDate(this.dateMidNight.getYear(), this.dateMidNight.getMonthOfYear(), this.dateMidNight.dayOfMonth().getMaximumValue());
	}

	public ASTDate nextDay() {
		return this.offSetDay(1);
	}

	public ASTDate prevDay() {
		return this.offSetDay(-1);
	}

	public ASTDate nextWeek() {
		return this.offSetWeek(1);
	}

	public ASTDate prevWeek() {
		return this.offSetWeek(-1);
	}

	public ASTDate nextMonth() {
		return this.offSetMonth(1);
	}

	public ASTDate prevMonth() {
		return this.offSetMonth(-1);
	}

	public ASTDate nextDateForIID(ASTEnum dateTypeIID) {
		switch (dateTypeIID) {
			case MONTHLY:
				return this.nextMonth();
			case WEEKLY:
				return this.nextWeek();
			case TWO_WEEK:
				return this.offSetWeek(2);
			default:
				return this.nextDay();
		}
	}

	public ASTDate prevDateForIID(ASTEnum dateTypeIID) {
		switch (dateTypeIID) {
			case MONTHLY:
				return this.prevMonth();
			case WEEKLY:
				return this.prevWeek();
			case TWO_WEEK:
				return this.offSetWeek(-2);
			default:
				return this.prevDay();
		}
	}

	public String dateString() {
		int day = this.dateMidNight.dayOfMonth().get();
		return day < 10 ? "0" + day : day + "";
	}

	public String shortMonthName() {
		if (this.monthName().length() >= 3) {
			return this.monthName().substring(0, 3);
		}
		return this.monthName();
	}

	public String monthName() {
		return this.dateMidNight.monthOfYear().getAsText();
	}

	public String dayName() {
		return this.dateMidNight.dayOfWeek().getAsText();
	}

	public String shortDayName() {
		return this.dateMidNight.dayOfWeek().getAsShortText();
	}

	public String yearAsText() {
		return this.dateMidNight.year().getAsText(Locale.US);
	}

	public int dayOfWeek() {
		int dayOfWeek = this.dateMidNight.dayOfWeek().get();
		return dayOfWeek == 7 ? 0 : dayOfWeek;
	}

	public int year() {
		return this.dateMidNight.year().get();
	}

	public int monthOfYear() {
		return this.dateMidNight.monthOfYear().get();
	}

	public int dayOfMonth() {
		return this.dateMidNight.dayOfMonth().get();
	}

	public int dayOfYear() {
		return this.dateMidNight.dayOfYear().get();
	}

	public ASTDate startOfWeek() {
		//return this.startOfWeek(ApplicationHelper.application().weekStartOffset());
		return  null;
	}

	public ASTDate startOfWeek(int weekStartOffset) {
		int offset = this.dayOfWeek(weekStartOffset);
		return new ASTDate(this.dateMidNight.minusDays(offset >= 0 ? offset : this.dateMidNight.getDayOfWeek() + weekStartOffset));
	}

	public int dayOfWeek(int weekStartOffset) {
		int dayOfWeek = this.dayOfWeek();
		int offset = dayOfWeek - weekStartOffset;
		if (offset < 0) {
			offset = 7 + offset;
		}
		return offset;
	}

	public ASTDate endOfWeek() {
		return this.startOfWeek().offSetDay(6);
	}

	public ASTDate endOfWeek(int offSet) {
		return this.startOfWeek(offSet).offSetDay(6);
	}

	public String weekRange() {
		ASTDate weekStart = this.startOfWeek();
		return weekStart.toDateSelection() + " - " + weekStart.offSetDay(6).toDateSelection();
	}

	public String twoWeekRange() {
		ASTDate weekStart = this.startOfWeek();
		return weekStart.toDateSelection() + " - " + weekStart.offSetWeek(1).offSetDay(6).toDateSelection();
	}

	public String monthString() {
		return ASTDateUtil.monthYearFormat().format(this.toDate());
	}

	public String dateString(ASTEnum dateTypeIID) {
		switch (dateTypeIID) {
			case MONTHLY:
				return this.monthString();
			case WEEKLY:
				return this.weekRange();
			case TWO_WEEK:
				return this.twoWeekRange();
			default:
				return this.toDateSelection();
		}
	}

	public String toCustFormat() {
		if (this.customerFormat == null) {
		//	this.customerFormat = ASTDateUtil.customerDateFormatter().format(this.toDate());
		}
		return this.customerFormat;
	}

	public String toRowFormat() {
		if (this.defaultRowFormat == null) {
			this.defaultRowFormat = ASTDateUtil.defaultRowFormatter().format(this.toDate());
		}
		return this.defaultRowFormat;
	}

	public String toHeaderFormat() {
		if (this.headerFormat == null) {
			this.headerFormat = ASTDateUtil.headerDateFormatter().format(this.toDate());
		}
		return this.headerFormat;
	}

	public String toServerFormat() {
		if (this.serverFormat == null) {
		//	this.serverFormat = ASTDateUtil.serverDateFormatter().format(this.toDate());
		}
		return this.serverFormat;
	}

	public String toMailFormat() {
		if (this.mailFormat == null) {
			this.mailFormat = ASTDateUtil.mailFormatter().format(this.toDate());
		}
		return this.mailFormat;
	}

	public String toDateSelection() {
		return ASTDateUtil.dateSelectionFormat().format(this.toDate());
	}

	@Override
	public int compareTo(@NonNull ASTDate date) {
		return this.dateMidNight.compareTo(date.dateMidNight);
	}

	public boolean before(ASTDate obj) {
		return obj != null && this.dateMidNight != null && this.compareTo(obj) < 0;
	}

	public boolean after(ASTDate obj) {
		return obj != null && this.dateMidNight != null && this.compareTo(obj) > 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof ASTDate && this.dateMidNight != null && this.compareTo((ASTDate) obj) == 0;
	}

	@Override
	public String toString() {
		return toDateSelection();
	}
}
