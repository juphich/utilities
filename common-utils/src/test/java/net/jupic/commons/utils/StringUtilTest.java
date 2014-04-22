package net.jupic.commons.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void testStringUtils() {
		String sample1 = "abcdefg";
		String sample2 = "한글ㅋ";
		String sample3 = "@$^sdifㅎ）";
		
		assertThat(StringUtils.getWeightLength(sample1), is(7));
		assertThat(StringUtils.getWeightLength(sample2), is(6));
		assertThat(StringUtils.getWeightLength(sample3), is(11));
	}
	
	@Test
	public void testShuffle() {
		String testText = "testTExt";
		
		assertNotSame(StringUtils.shuffle(testText), testText);
	}
	
	@Test
	public void testHexConverting1() {
		String testHex = "0xFEA41C23FF";
		
		byte[] result = StringUtils.hexToBytes(testHex);
		
		StringBuilder text = new StringBuilder("0x");
		for (byte s : result) {
			text.append(Integer.toHexString(0xff & s).toUpperCase());
		}
		
		assertEquals(testHex, text.toString());
	}
	
	@Test
	public void testHexConverting2() {
		byte[] input = new byte[]{0x12, 0x0A, 0x7D};
		
		String result = StringUtils.bytesToHex(input);
		
		assertEquals(result, "120a7d");
	}
}
