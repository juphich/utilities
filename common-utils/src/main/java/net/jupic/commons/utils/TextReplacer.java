package net.jupic.commons.utils;

import net.jupic.commons.exception.UtilityUsageException;

/**
 * @author Chang jung pil
 *
 */
public class TextReplacer {

	private String text;
	private String pattern;
	private String replacement;
	
	private int offset;
	private int length;
	
	private String reservedPattern = "|";
	
	public TextReplacer(String text) {
		this.text = text;
	}
	
	public TextReplacer setOffset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public TextReplacer setLength(int length) {
		this.length = length;
		return this;
	}
	
	public TextReplacer setIndex(int offset, int length) {
		this.offset = offset;
		this.length = length;
		return this;
	}
	
	public TextReplacer setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}
	
	public TextReplacer setReplacement(String replacement) {
		if (reservedPattern.equals(replacement)) {
			throw new UtilityUsageException("'|' is reserved key word. could not set to replacement");
		}
		
		this.replacement = replacement;
		return this;
	}
	
	public String replace() {
		if (this.pattern != null && (this.length != 0 || this.offset != 0)) {
			throw new UtilityUsageException("Pattern and Index could not set together.");
		}
		
		if (pattern != null) {
			return replaceByPattern();
		} else if (length != 0 || offset != 0) {
			return replaceByIndex();
		} else {
			return this.text;
		}
	}
	
	public String getOriginalText() {
		return this.text;
	}
	
	private String replaceByIndex() {
		if (this.replacement == null) {
			throw new UtilityUsageException("replacement character or string has to be set");
		}
		
		StringBuilder tempString = new StringBuilder();
		String reslut = null;
		
		int counter = this.length == 0 ? text.length() : this.length;
		char[] characters = text.toCharArray();
		
		if (this.length >= 0) {
			for (int i = 0; i < characters.length; i++) {
				if (i >= offset && counter != 0) {
					tempString.append(reservedPattern);
					counter--;
				} else {
					tempString.append(characters[i]);
				}				
			}
			
			reslut = tempString.toString();
		} else {
			int startPos = this.offset == 0 ? text.length() : this.offset;
			
			for (int i = characters.length - 1; i >= 0; i--) {
				if (i <= startPos && counter != 0) {
					tempString.append(reservedPattern);
					counter++;
				} else {
					tempString.append(characters[i]);
				}				
			}
			
			reslut = tempString.reverse().toString();
		}
		
		return reslut.replace(reservedPattern, replacement);
	}
	
	private String replaceByPattern() {
		if (this.replacement == null) {
			throw new UtilityUsageException("to character or string has to be set");
		}
		
		return text.replaceAll(pattern, replacement);
	}
}
