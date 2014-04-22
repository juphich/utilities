package net.jupic.commons.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidationUtilTest {

	@Test
	public void testSpescailCharacter() {
		String special = "#special test value !!!";
		String phrase  = "normal test value 1234";
		String word = "testword1234";
		
		assertTrue(ValidationUtils.isIncludeSpecialChar(special));
		assertTrue(ValidationUtils.isIncludeSpecialChar(phrase));
		assertFalse(ValidationUtils.isIncludeSpecialChar(word));
	}
}
