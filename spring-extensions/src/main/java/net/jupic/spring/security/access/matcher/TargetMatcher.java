package net.jupic.spring.security.access.matcher;

/**
 * 접근 제한 대상 Matcher
 * 
 * @author chang jung pil
 *
 */
public interface TargetMatcher {
	boolean matches(String pattern);
}
