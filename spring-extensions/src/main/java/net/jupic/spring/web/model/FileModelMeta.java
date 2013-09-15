package net.jupic.spring.web.model;

import java.io.IOException;
import java.io.InputStream;

import net.jupic.spring.web.exception.UndefinedSizeException;


public interface FileModelMeta {
	String getFilename();
	long getSize() throws UndefinedSizeException;
	InputStream getInputStream() throws IOException;
}
