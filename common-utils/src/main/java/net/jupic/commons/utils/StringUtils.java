package net.jupic.commons.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class StringUtils {

	public static String getRandomString(int length, String sample) {
		Random random = new Random(); 
		
		String result = "";
		for (int i = 0; i < length; i++) {
			int idx = random.nextInt(sample.length() - 1);
			result = result.concat(sample.substring(idx, idx + 1));
		}
		return result;
	}
	
	public static String getRandomString(int length) {
		return getRandomString(length, new String("ABCDEFGHJKLMNPQRSTUVWXYZ23456789"));
	}
	
	/**
	 * 날짜값이 포함 된 25자리의 랜덤키를 발급한다. (날짜12+랜덤13)
	 */
	public static String getTicketRandomKey() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		return format.format(new Date(System.currentTimeMillis())) + StringUtils.getRandomString(13).toLowerCase();
	}
	
	public static String convertIdentityNoToBirthdate(String identityNo) {
		return ((Integer.parseInt(identityNo.substring(6, 7)) < 3) ? "19" : "20") + identityNo.substring(0, 6);
	}
	
	public static String getGenderFromIdentityNo(String identityNo) {
		return (Integer.parseInt(identityNo.substring(6, 7)) % 2 == 1) ? "m" : "f";
	}
	
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}
	
	/**
	 * ascii 문자는 1자리 유니코드 문자는 2자리로 계산한다.
	 * 
	 * @param string
	 * @return
	 */
	public static int getWeightLength(String string) {
		char[] characters = string.toCharArray();
		
		int count = 0;
		
		for (char word : characters) {
			if (word <= 0x7F) {
				count++;
			} else {
				count += 2;
			}
		}
		
		return count;
	}
	
	public static String shuffle(String string) {
		List<Character> characters = new ArrayList<Character>();
		for (char ch : string.toCharArray()) {
			characters.add(ch);
		}
		
		Collections.shuffle(characters);
		
		StringBuilder buffer = new StringBuilder();
		for (char ch : characters) {
			buffer.append(ch);
		}
		
		return buffer.toString();
	}
	
	/**
	 * 16 진수 스트링 값을 byte 배열로 변환한다.
	 * 변환 단위는 byte(8bit, 0x00 ~ 0xFF)이므로 입력 문자열은 반드시 짝수여야 한다. (16진수 한 문자는 4bit 를 차지한다.)
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexToBytes(final String hex) {
		char[] arrays;
		if (hex.startsWith("0x")) {
			arrays = hex.substring(2, hex.length()).toCharArray();
		} else {
			arrays = hex.toCharArray();
		}
		
		if (arrays.length % 2 != 0) {
			throw new IllegalArgumentException("it's invalid hex data [unit is 8bit].");
		}
		
		byte[] result = new byte[arrays.length / 2];
		int i=0;
		while (i < arrays.length) {
			int first = Character.digit(arrays[i], 16);
			int second = Character.digit(arrays[i+1], 16);
			
			if (first < 0 || second < 0) {
				throw new IllegalArgumentException("it's invalid hex data [" + arrays[i] + arrays[i+1] + "]");
			}
			
			result[i/2] = (byte)(first * 16 + second);
			i += 2;
		}
		return result;
	}
	
	/**
	 * byte 배열을 hex 스트링으로 변환한다.
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(final byte[] bytes) {
		StringBuilder result = new StringBuilder();
		
		String temp;
		for (byte b : bytes) {
			temp = "0" + Integer.toHexString(0xff & b);
			result.append(temp.substring(temp.length() - 2));
		}
		
		return result.toString();
	}
}
