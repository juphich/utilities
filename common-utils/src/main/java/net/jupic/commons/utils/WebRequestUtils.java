package net.jupic.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Chang jung pil
 *
 */
public class WebRequestUtils {

	public static Cookie obtainCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	public static boolean doesExistCookie(HttpServletRequest request, String cookieName) {
		return (obtainCookie(request, cookieName) != null);
	}

	public static void expireCookies(HttpServletRequest request, HttpServletResponse response, String... cookieNames) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return;
		}
		
		for (Cookie cookie : cookies) {
			for (String cookieName : cookieNames) {
				if (cookie.getName().equals(cookieName)) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
	}
	
	public static String getRemoteIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || ip.toLowerCase().equals("unknown")) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		
		if (ip == null || ip.length() == 0 || ip.toLowerCase().equals("unknown")) {
			ip = request.getRemoteAddr();
		}
		
		return ip;
	}
}
