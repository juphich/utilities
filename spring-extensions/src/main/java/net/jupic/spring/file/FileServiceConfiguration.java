package net.jupic.spring.file;

/**
 * @author chang jung pil
 *
 */
public interface FileServiceConfiguration {

	public String getFileUploadPath();
	
	public long getMaximumFileSize();
	
	public String[] getAllowedFileExtensions();
	
	public String[] getDisAllowedFileExtensions();
	
	public Policy getUploadPolicy();
	
	public boolean doseKeepOriginalFileName();
}
