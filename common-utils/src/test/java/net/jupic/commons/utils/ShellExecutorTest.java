package net.jupic.commons.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class ShellExecutorTest {

	@Test
	public void testShellCommand() {
		String result = ShellExecutor.execute("echo this is test message");
		assertThat(result.trim(), is("this is test message"));
	}
}
