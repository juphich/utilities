package net.jupic.spring.file;

/**
 * @author chang jung pil
 *
 */
public class GenericFileServiceConfiguration implements FileServiceConfiguration {

	private String fileUploadPath;
	
	private long maximumFileSize;
	
	private String[] allowedFileExtensions;
	
	private String[] disAllowedFileExtensions;
	
	private Policy fileUploadPolicy = Policy.DENY_ONLY_DISALLOWED;
	
	private boolean keepingOriginalFileName = false;

	@Override
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	@Override
	public long getMaximumFileSize() {
		return maximumFileSize;
	}

	@Override
	public String[] getAllowedFileExtensions() {
		return allowedFileExtensions;
	}

	@Override
	public String[] getDisAllowedFileExtensions() {
		return disAllowedFileExtensions;
	}

	@Override
	public Policy getUploadPolicy() {
		return fileUploadPolicy;
	}
	
	@Override
	public boolean doseKeepOriginalFileName() {
		return keepingOriginalFileName;
	}

	public void setMaximumFileSize(long maximumFileSize) {
		this.maximumFileSize = maximumFileSize;
	}

	public Policy getFileUploadPolicy() {
		return fileUploadPolicy;
	}

	public void setFileUploadPolicy(Policy fileUploadPolicy) {
		this.fileUploadPolicy = fileUploadPolicy;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public void setAllowedFileExtensions(String[] allowedFileExtensions) {
		this.allowedFileExtensions = allowedFileExtensions;
	}
	
	public void setAllowedFileExtensions(String allowedFileExtensions) {
		String[] fileExtensions = allowedFileExtensions.toLowerCase().split("[,;\\s\\/]+");
		setAllowedFileExtensions(fileExtensions);
	}

	public void setDisAllowedFileExtensions(String[] disAllowedFileExtensions) {
		this.disAllowedFileExtensions = disAllowedFileExtensions;
	}
	
	public void setDisAllowedFileExtensions(String disAllowedFileExtentions) {
		String[] fileExtensions = disAllowedFileExtentions.toLowerCase().split("[,;\\s\\/]+");
		setDisAllowedFileExtensions(fileExtensions);
	}
	
	public void setKeepingOriginalFileName(boolean keepingOriginalFileName) {
		this.keepingOriginalFileName = keepingOriginalFileName;
	}
}
