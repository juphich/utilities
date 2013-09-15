package net.jupic.commons.utils;

import java.io.File;

/**
 * @author chang jung pil
 *
 */
public class FilePathBuilder {

	private StringBuilder pathBuilder;
	private String separator;
	
	public FilePathBuilder(String defaultPath) {
		this.pathBuilder = new StringBuilder(defaultPath);
		this.separator = File.separator;
	}
	
	public FilePathBuilder(String defaultPath, String separator) {
		this.pathBuilder = new StringBuilder(defaultPath);
		this.separator = separator;
	}
	
	public FilePathBuilder append(String path) {
		trimPath();
		pathBuilder.append(separator).append(trimPath(path));
		return this;
	}
	
	public String getPath() {
		return pathBuilder.toString();
	}
	
	private void trimPath() {
		int lastIndex = pathBuilder.length() - 1;
		if (pathBuilder.lastIndexOf("/") == lastIndex || pathBuilder.lastIndexOf("\\") == lastIndex) {
			pathBuilder.deleteCharAt(lastIndex);
		}
	}
	
	private String trimPath(String path) {
		if (path.startsWith("/") || path.startsWith("\\")) {
			return path.substring(1);
		}
		return path;
	}

	public String from(String path) {
		int index = pathBuilder.indexOf(trimPath(path));
		return pathBuilder.substring(index);
	}

	public String after(String path) {
		int index = pathBuilder.indexOf(path) + path.length();
		return trimPath(pathBuilder.substring(index));
	}
}
