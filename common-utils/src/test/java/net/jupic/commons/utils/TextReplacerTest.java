package net.jupic.commons.utils;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.core.Is.is;

import net.jupic.commons.exception.UtilityUsageException;
import net.jupic.commons.utils.TextReplacer;

import org.junit.Test;


/**
 * @author Chang jung pil
 *
 */
public class TextReplacerTest {

	@Test
	public void testTextReplacerWithIndex() {
		String test1 = new TextReplacer("my text data").setIndex(3, 5).setReplacement("*").replace();
		assertThat(test1, is("my *****data"));
		
		String test2 = new TextReplacer("my text data").setIndex(7, -3).setReplacement("@*").replace();
		assertThat(test2, is("my te@*@*@*data"));
		
		String test3 = new TextReplacer("my text data").setOffset(4).setReplacement("*").replace();
		assertThat(test3, is("my t********"));
		
		String test4 = new TextReplacer("my text data").setLength(-5).setReplacement("-*").replace();
		assertThat(test4, is("my text-*-*-*-*-*"));
		
		String test5 = new TextReplacer("my text data").setLength(5).setReplacement("*").replace();
		assertThat(test5, is("*****xt data"));
	}
	
	@Test
	public void testTextReplacerWithPattern() {
		String test1 = new TextReplacer("my text data").setPattern("text").setReplacement("****").replace();
		assertThat(test1, is("my **** data"));
		
		String test2 = new TextReplacer("my text data").setPattern("\\s").setReplacement("*").replace();
		assertThat(test2, is("my*text*data"));
		
		String test3 = new TextReplacer("my text data").setPattern("t+[a-z]*t+").setReplacement("*").replace();
		assertThat(test3, is("my * data"));
	}
	
	@Test
	public void testTextReplacerException() {
		try {
			new TextReplacer("my text data").setPattern("data").setIndex(0, 1).replace();
			fail();
		} catch (UtilityUsageException e) {
		}
		
		try {
			new TextReplacer("my text data").setPattern("data").setOffset(1).replace();
			fail();
		} catch (UtilityUsageException e) {
		}
		
		try {
			new TextReplacer("my text data").setPattern("data").setLength(5).replace();
			fail();
		} catch (UtilityUsageException e) {
		}
		
		try {
			new TextReplacer("my text data").setReplacement("|");
			fail();
		} catch (UtilityUsageException e) {
		}
		
		try {
			new TextReplacer("my text data").setPattern("t+[a-z]*t+").replace();
			fail();
		} catch (UtilityUsageException e) {
		}
	}
}
