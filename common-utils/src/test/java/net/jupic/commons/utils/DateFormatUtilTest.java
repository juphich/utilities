package net.jupic.commons.utils;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 
 * 오늘날짜 
 * 날짜 포맷
 * 
 * 오늘날짜 연산
 * 
 * 특정 날짜 날짜 포맷 연산
 * 
 * <formatDate var="today"/>
 * <formatDate value="2012-02-23" format="yyyy-MM-dd" var="today"/>
 * 
 * <formatDate format="yyyy-MM-dd" value="addDays(#today, 1)"/>
 * <formatDate format="yyyy-MM-dd" value="addMonths(#today, -3)"/>
 * <formatDate format="yyyy-MM-dd" value="addYears(#today, -3)"/>
 * 
 * @author jupic
 *
 */
public class DateFormatUtilTest {

	@Test
	public void testDateFormatUtil() {
		DateFormatUtils util = new DateFormatUtils();
		Date today = util.getDate();
		
		Date yesterday = util.getDate("2012-07-02 00:00:01");
		
		assertTrue(today.compareTo(yesterday) > 0);
	}
	
	@Test
	public void testDateOperation() {
		DateFormatUtils util = new DateFormatUtils();
		
		Date basisTime = util.getDate("2012-01-01 00:00:00");
		
		Date second = util.addSeconds(basisTime, 10);
		assertEquals(util.getDate("2012-01-01 00:00:10"), second);
		
		Date minute = util.addMinutes(basisTime, 70);
		assertEquals(util.getDate("2012-01-01 01:10:00"), minute);
		
		Date hour = util.addHours(basisTime, -7);
		assertEquals(util.getDate("2011-12-31 17:00:00"), hour);
		
		Date day = util.addDays(basisTime, 45);
		assertEquals(util.getDate("2012-02-15 00:00:00"), day);
		
		Date month = util.addMonths(basisTime, 6);
		assertEquals(util.getDate("2012-07-01 00:00:00"), month);
		
		Date year = util.addYears(basisTime, -8);
		assertEquals(util.getDate("2004-01-01 00:00:00"), year);
	}
	
	@Test
	public void testSpelTest() {
		DateFormatUtils util = new DateFormatUtils();
		Date basisDate = util.getDate("2012-01-01 00:00:00");
		
		EvaluationContext dateContext = new StandardEvaluationContext(util);
		dateContext.setVariable("today", basisDate);
		
		ExpressionParser parser = new SpelExpressionParser();
		Date result = parser.parseExpression("addSeconds(#today, 30)").getValue(dateContext, Date.class);
		
		assertEquals(util.getDate("2012-01-01 00:00:30"), result);
	}
}
