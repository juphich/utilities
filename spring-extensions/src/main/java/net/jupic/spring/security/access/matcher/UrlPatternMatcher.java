package net.jupic.spring.security.access.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlPatternMatcher implements TargetMatcher {

	private String pattern;
	
	public UrlPatternMatcher(String pattern) {
		patternInitialize(pattern);
	}
	
	private void patternInitialize(String pattern) {
		StringBuilder patternBuilder = new StringBuilder();
		
		if (pattern.charAt(0) != '/') {
			patternBuilder.append("/");
		}
		
		char[] patterns = pattern.toCharArray();
		
		boolean containParameter = false;
		
		for (int i=0; i<patterns.length; i++) {
			if (i<patterns.length-1 && patterns[i] == '*' && patterns[i+1] == '*') {
				patternBuilder.append("([\\w\\-_]+/?)*([\\-\\.\\?&%=\\w]+)");
				i++;
			} else if (patterns[i] == '*') {
				patternBuilder.append("[\\-\\.\\?&%=\\w]");
				if (i != 0 && patterns[i-1] == '/') {
					patternBuilder.append("+");
				} else {
					patternBuilder.append("*");
				}
			} else if (patterns[i] == '?') {
				patternBuilder.append("\\?");
				containParameter = true;
			} else if (!containParameter && (patterns.length - 1 == i)) {
				patternBuilder.append(patterns[i]).append("/?");
			} else {
				patternBuilder.append(patterns[i]);
			}
		}
		
		patternBuilder.append("$");
		this.pattern = patternBuilder.toString();
	}
	
	@Override
	public boolean matches(String targetPattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(targetPattern);
		
		return m.find();
	}
}
