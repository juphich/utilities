package net.jupic.commons.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import net.jupic.commons.utils.FilePathBuilder;

import org.junit.Test;

public class PathBuilderTest {

	@Test
	public void testPathBuilder() {
		String path1 = new FilePathBuilder("/data/upload", "/")
			.append("/test/").append("/sample").append("file").getPath();
		
		assertThat(path1, is("/data/upload/test/sample/file"));
		
		String path2 = new FilePathBuilder("/data/image/", "/")
			.append("/my/").append("/sample").append("file/").getPath();
		
		assertThat(path2, is("/data/image/my/sample/file/"));
	}
	
	@Test
	public void testRelativerPath() {
		String fullPath = "/data/upload/test/sample/file";
		
		String path1 = new FilePathBuilder(fullPath).from("test");
		assertThat(path1, is("test/sample/file"));
		
		String path2 = new FilePathBuilder(fullPath).after("/upload");
		assertThat(path2, is("test/sample/file"));
	}
}
