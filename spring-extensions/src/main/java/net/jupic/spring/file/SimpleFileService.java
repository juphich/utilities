package net.jupic.spring.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import net.jupic.commons.utils.FilePathBuilder;
import net.jupic.spring.file.exception.FileSizeOverException;
import net.jupic.spring.file.exception.FileUploadException;
import net.jupic.spring.file.exception.FileUploadPolicyViolationException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author chang jung pil
 *
 */
public class SimpleFileService implements FileService, InitializingBean {

	private FileServiceConfiguration configuration;
	
	@Deprecated
	public SimpleFileService() {};
	
	public SimpleFileService(FileServiceConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Autowired(required=false)
	public void setConfiguration(FileServiceConfiguration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public FileMetadata uploadFile(MultipartFile file) {
		return uploadFile(file, null);
	}

	@Override
	public FileMetadata uploadFile(MultipartFile file, String additionalPath) {
		String fileName = file.getOriginalFilename();
		long   fileSize = file.getSize();
		String extension = getFileExtension(fileName);
	
		FileMetadata fileInfo = new FileMetadata();
		fileInfo.setLogicalName(fileName);
		fileInfo.setPhysicalName(fileName);
		fileInfo.setSize(fileSize);
		fileInfo.setExtension(extension);
		
		try {
			upload(fileInfo, additionalPath, file.getInputStream());
			
			return fileInfo;
		} catch (IOException e) {
			throw new FileUploadException("Fail to file upload.", e);
		}
	}
	
	protected void upload(FileMetadata fileInfo, String additionalPath, InputStream in) throws FileNotFoundException, IOException {
		long fileSize = fileInfo.getSize();
		
		long maxSize = configuration.getMaximumFileSize();
		if (maxSize > 0 && maxSize < fileSize) {
			throw new FileSizeOverException("Upload file size must not be over " + maxSize + " : size - " + fileSize);
		}
		
		checkUploadPolicy(fileInfo);
		
		String uploadDirectory = makeUploadDirectory(fileInfo, additionalPath);
		
		String fullName = null;
		if (!configuration.doseKeepOriginalFileName()) {
			fullName = uploadDirectory + File.separator + getRandomString(5);
		} else {
			fullName = uploadDirectory + File.separator + fileInfo.getLogicalName();
		}
		
		FileCopyUtils.copy(in, new FileOutputStream(fullName));
	}
	
	private void checkUploadPolicy(FileMetadata fileInfo) {
		Policy policy = configuration.getUploadPolicy();
		
		if (policy.equals(Policy.ACCEPT_ONLY_ALLOWED)) {
			if (!isAllowedFile(fileInfo)) {
				throw new FileUploadPolicyViolationException("Could not upload file : '" + fileInfo.getLogicalName() + "' is not accepted file.");
			}
		} else if (policy.equals(Policy.DENY_ONLY_DISALLOWED)) {
			if (isDisallowedFile(fileInfo)) {
				throw new FileUploadPolicyViolationException("Could not upload file : '" + fileInfo.getLogicalName() + "' is denied file.");
			}
		} else if (policy.equals(Policy.APPLY_FULL_POLICY)) {
			if (!isAllowedFile(fileInfo) || isDisallowedFile(fileInfo)) {
				throw new FileUploadPolicyViolationException("Could not upload file : '" + fileInfo.getLogicalName() + "' is not allowed file.");
			}
		} else if (policy.equals(Policy.DENY_ALL)) {
			throw new FileUploadPolicyViolationException("This service denies all files uploading.");
		}
	}

	@Override
	public void deleteFile(File file) {
		file.delete();
	}

	@Override
	public void deleteFile(String fileName) {
		deleteFile(new File(fileName));
	}

	@Override
	public boolean isAllowedFile(FileMetadata filename) {
		String[] extensions = configuration.getAllowedFileExtensions();
		for(String extention : extensions) {
			if (extention.equals(filename.getExtension().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDisallowedFile(FileMetadata filename) {
		String[] extensions = configuration.getDisAllowedFileExtensions();
		for(String extention : extensions) {
			if (extention.equals(filename.getExtension().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public FileServiceConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public final void afterPropertiesSet() throws Exception {
		initialize();
	}
	
	protected String makeUploadDirectory(FileMetadata fileInfo, String additionalPath) {
		String group = getGroupingPath();
		
		FilePathBuilder pathBuilder = new FilePathBuilder(configuration.getFileUploadPath());
		if (additionalPath != null && additionalPath.length() > 0) {
			pathBuilder.append(additionalPath);
		}
		pathBuilder.append(group);
		
		String uploadPath = pathBuilder.getPath();
		
		File uploadDirectory = new File(uploadPath);
		if(!uploadDirectory.exists()) {
			uploadDirectory.mkdirs();
		}
		
		fileInfo.setLocation(uploadPath);
		
		return uploadPath;
	}
	
	protected String getGroupingPath() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	private String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
	}
	
	private String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
         
        String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
         
        for ( int i=0 ; i<length ; i++ ) {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }
	
	protected void initialize() {
		Assert.notNull(configuration, "FileServiceConfiguration must be set.");
	}
}
