package net.jupic.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormatUtils {

	private static Logger log = LoggerFactory.getLogger(DateFormatUtils.class);
	
	private Calendar calendar = new GregorianCalendar();
	
	private String pattern;
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Date getDate() {
		return new Date();
	}

	public Date getDate(String dateValue) {
		String usePattern = null;
		
		if (pattern != null) { 
			usePattern = pattern;			
		} else if (Pattern.matches("^\\d{4}\\-((0\\d)|(1[12]))\\-(([012]\\d)|(3[01]))$", dateValue)) {
			usePattern = "yyyy-MM-dd";
		} else if (Pattern.matches(
				"^\\d{4}\\-((0\\d)|(1[12]))\\-(([012]\\d)|(3[01]))\\s(([01]\\d)|(2[0-3])):[0-5]\\d:[0-5]\\d$", 
				dateValue)) {
			usePattern = "yyyy-MM-dd hh:mm:ss";
		} else {
			usePattern = "yyyy-MM-dd";
		}
		
		return getDate(dateValue, usePattern);
	}

	public Date getDate(String dateValue, String pattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			return format.parse(dateValue);
		} catch (ParseException e) {
			if (log.isDebugEnabled()) {
				log.debug(e.getMessage());
			}
			return null;
		}
	}

	public Date addSeconds(int interval) {
		return add(getCurrentDate(), Calendar.SECOND, interval);
	}
	
	public Date addSeconds(Date date, int interval) {
		return add(date, Calendar.SECOND, interval);
	}

	public Date addMinutes(int interval) {
		return add(getCurrentDate(), Calendar.MINUTE, interval);
	}
	
	public Date addMinutes(Date date, int interval) {
		return add(date, Calendar.MINUTE, interval);
	}

	public Date addHours(int interval) {
		return add(getCurrentDate(), Calendar.HOUR, interval);
	}
	
	public Date addHours(Date date, int interval) {
		return add(date, Calendar.HOUR, interval);
	}

	public Date addDays(int interval) {
		return add(getCurrentDate(), Calendar.DATE, interval);
	}
	
	public Date addDays(Date date, int interval) {
		return add(date, Calendar.DATE, interval);
	}

	public Date addMonths(int interval) {
		return add(getCurrentDate(), Calendar.MONTH, interval);
	}

	public Date addMonths(Date date, int interval) {
		return add(date, Calendar.MONTH, interval);
	}

	public Date addYears(int interval) {
		return add(getCurrentDate(), Calendar.YEAR, interval);
	}

	public Date addYears(Date date, int interval) {
		return add(date, Calendar.YEAR, interval);
	}
	
	private Date add(Date date, int calendarField, int interval) {
		calendar.setTime(date);
		calendar.add(calendarField, interval);
		
		return calendar.getTime();
	}
	
	public String formatDate(Date date, String pattern) {
		if (pattern == null) {
			pattern = "yyyy-MM-dd";
		}
		
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date getCurrentDate() {
		return new Date(Calendar.getInstance().getTimeInMillis());
	}
}
