package net.jupic.spring.file;

import java.io.File;
import java.io.Serializable;

/**
 * @author chang jung pil
 *
 */
public class FileMetadata implements Serializable {

	private static final long serialVersionUID = -3938025609905513744L;

	private String logicalName;
	
	private String physicalName;
	
	private String extension;
	
	private long   size;
	
	private String location;

	public String getLogicalName() {
		return logicalName;
	}

	public String getPhysicalName() {
		return physicalName;
	}

	public String getExtension() {
		return extension;
	}

	public long getSize() {
		return size;
	}

	public String getLocation() {
		return location;
	}
	
	public String getFullName() {
		return location + File.separator + logicalName;
	}
	
	public String getPhysicalFullPath() {
		return location + File.separator + physicalName;
	}

	public void setLogicalName(String name) {
		this.logicalName = name;
	}

	public void setPhysicalName(String physicalName) {
		this.physicalName = physicalName;
	}

	public void setFilesize(long filesize) {
		this.size = filesize;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
