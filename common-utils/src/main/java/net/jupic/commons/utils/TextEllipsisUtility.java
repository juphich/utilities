package net.jupic.commons.utils;

/**
 * @author chang jung pil
 *
 */
public class TextEllipsisUtility {

	private String text;
	
	private String keyword;
	
	private int length;
	
	private int position;
	
	private boolean dots;

	
	public TextEllipsisUtility(String text, int length) {
		this.text = text;
		this.length = length;
	}

	public TextEllipsisUtility setDots(boolean dots) {
		this.dots = dots;
		return this;
	}

	public TextEllipsisUtility setKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}
	
	public TextEllipsisUtility setPosition(int position) {
		this.position = position;
		return this;
	}
	
	public String cut() {
		if (text.length() <= length) {
			return text;
		} else {
			int startIndex = (keyword != null && text.indexOf(keyword) > -1) ? text.indexOf(keyword) : 0;
			startIndex = startIndex - position < 0 ? 0 : startIndex - position;
			
			String result = text.substring(startIndex, startIndex + length);
			result = dots ? result + "..." : result;
			
			return result;
		}
	}
}
