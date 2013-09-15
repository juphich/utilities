package net.jupic.spring.file;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import net.jupic.spring.file.GenericFileServiceConfiguration;

import org.junit.Test;


public class FileConfigurationTest {

	@Test
	public void fileConfigurationTest() {
		String fileExtentions = "jpg,bmp  /  exe;com ,java";
		
		GenericFileServiceConfiguration configuration = new GenericFileServiceConfiguration();
		configuration.setAllowedFileExtensions(fileExtentions);
		
		String[] allowed = configuration.getAllowedFileExtensions();
		assertThat(allowed.length, is(5));
	}
}
