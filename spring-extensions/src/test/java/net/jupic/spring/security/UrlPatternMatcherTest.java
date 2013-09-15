package net.jupic.spring.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.jupic.spring.security.access.matcher.UrlPatternMatcher;

import org.junit.Test;


public class UrlPatternMatcherTest {

	@Test
	public void testPatternType1() {
		UrlPatternMatcher matcher = new UrlPatternMatcher("/test/result/**");
		
		assertFalse(matcher.matches("/test/result"));
		assertFalse(matcher.matches("/test/result/"));
		assertTrue(matcher.matches("/test/result/goal"));
		assertTrue(matcher.matches("/test/result/my/goal"));
		assertTrue(matcher.matches("/test/result/my/goal?param=12"));
	}
	
	@Test
	public void testPatternType2() {
		UrlPatternMatcher matcher = new UrlPatternMatcher("/test/result/*");
		
		assertFalse(matcher.matches("/test/result"));
		assertFalse(matcher.matches("/test/result/"));
		assertTrue(matcher.matches("/test/result/goal"));
		assertTrue(matcher.matches("/test/result/goal?param=12&no=2"));
		assertFalse(matcher.matches("/test/result/my/gole"));
	}
	
	@Test
	public void testPatternType3() {
		UrlPatternMatcher matcher3 = new UrlPatternMatcher("/test/**/goal");
		
		assertFalse(matcher3.matches("/test/goal"));
		assertTrue(matcher3.matches("/test/result/goal"));
		assertTrue(matcher3.matches("/test/result/goal/"));
		assertTrue(matcher3.matches("/test/result/my/goal"));
		assertFalse(matcher3.matches("/test/result/goal?for=test"));
		assertFalse(matcher3.matches("/test/result/goal-test"));
	}
	
	@Test
	public void testPatternType4() {
		UrlPatternMatcher matcher = new UrlPatternMatcher("/test/*/goal");
		
		assertTrue(matcher.matches("/test/result/goal"));
		assertTrue(matcher.matches("/test/result/goal/"));
		assertFalse(matcher.matches("/test/result/my/goal"));
		assertFalse(matcher.matches("/test/result/my/goal/"));
		assertFalse(matcher.matches("/test/result/goal?for=test"));
		assertFalse(matcher.matches("/test/result/goal-test"));
	}
	
	@Test
	public void testPatternType5() {
		UrlPatternMatcher matcher = new UrlPatternMatcher("/test/*/goal*");
		
		assertTrue(matcher.matches("/test/result/goal"));
		assertTrue(matcher.matches("/test/result/goal-test"));
		assertTrue(matcher.matches("/test/result/goal?for=test"));
		assertFalse(matcher.matches("/test/result/my/goal"));		
	}
	
	@Test
	public void testPatternType6() {
		UrlPatternMatcher matcher = new UrlPatternMatcher("/test/**/goal*");		
		
		assertFalse(matcher.matches("/test/goal"));
		assertTrue(matcher.matches("/test/result/goal"));
		assertTrue(matcher.matches("/test/result/goal-test"));
		assertTrue(matcher.matches("/test/result/goal?for=test"));
		assertTrue(matcher.matches("/test/result/my/goal?for=test"));		
	}
}
