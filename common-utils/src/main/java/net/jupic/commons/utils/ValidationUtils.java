package net.jupic.commons.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

	public static boolean isNumber(String val) {
		Pattern pattern = Pattern.compile("[\\d]*");  
		boolean isNumber = pattern.matcher(val).matches();
		return isNumber;
	}
	
	public static boolean isEmail(String val) {
		Pattern pattern = Pattern.compile("^[\\w\\-]([\\.\\w\\-])+[\\w\\-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);  
		boolean isEmail = pattern.matcher(val).matches();
		return isEmail;
	}
	
	public static boolean isCellphone(String val) {
		val = val.replace("-", "");
		return (isNumber(val) && (val.length() == 10 || val.length() == 11) 
				&& new String("010/011/016/017/018/019").indexOf(val.substring(0, 3)) > -1);
	}
	
	public static boolean isIdentityNo(String val) {		
		if (val.length() != 13 || !ValidationUtils.isNumber(val)) { return false; }		
		if (!('1' <= val.charAt(6) && val.charAt(6) <= '4')) { return false; }
		
		int[] multipler = { 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5 };
		int sum = 0;
		for (int i = 0; i < val.length() - 1; i++) {
			sum += Integer.parseInt(val.substring(i, i + 1)) * multipler[i];
		}
		
		int mod = (11 - (sum % 11)) % 10;
		if (Integer.parseInt(val.substring(val.length() - 1, val.length())) != mod) {
			return false; 
		} else {
			String birth = ((val.charAt(6) == '1' || val.charAt(6) == '2') ? "19" : "20") + val.substring(0, 6);
			DateFormatUtils dateUtil = new DateFormatUtils();
			Date birthDate = dateUtil.getDate(birth, "yyyyMMdd");
			if (birthDate == null || birthDate.compareTo(DateFormatUtils.getCurrentDate()) > 0) {
				// 날짜 형식이 아니거나 현재 날짜보다 미래의 생년월일이면 오류
				return false; 
			}
		}
		return true;
	}
	
	public static boolean isIncludeSpecialChar(String val) {
		Pattern p = Pattern.compile("[^(0-9a-zA-Zㄱ-ㅎ가-힣)]");
		Matcher m = p.matcher(val);
		return m.find();
	}
	
	public static boolean isEventCode(String val) {
		if (val.length() > 30) return false;
		val = val.replace("-", "");
		Pattern pattern = Pattern.compile("^[0-9a-z]+$", Pattern.CASE_INSENSITIVE);
		boolean isEventCode = pattern.matcher(val).matches();
		return isEventCode;
	}
}
