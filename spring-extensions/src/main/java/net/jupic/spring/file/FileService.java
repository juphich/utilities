package net.jupic.spring.file;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author chang jung pil
 *
 */
public interface FileService {

	public FileMetadata uploadFile(MultipartFile file);
	
	public FileMetadata uploadFile(MultipartFile file, String additionalPath);
	
	public void deleteFile(File file);
	
	public void deleteFile(String fileName);
	
	public boolean isAllowedFile(FileMetadata file);
	
	public boolean isDisallowedFile(FileMetadata file);
	
	public FileServiceConfiguration getConfiguration();
}
