package net.jupic.spring.file;

import net.jupic.spring.file.exception.FileUploadException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author chang jung pil
 *
 */
public class FileServiceDaoSupport extends SimpleFileService {
	
	@SuppressWarnings("rawtypes")
	private FileDao fileDao;

	@SuppressWarnings("deprecation")
	public FileServiceDaoSupport() {};
	
	public FileServiceDaoSupport(FileServiceConfiguration configuration) {
		super(configuration);
	}
	
	@Autowired(required=false)
	public void setFileDao(FileDao<?> fileDao) {
		this.fileDao = fileDao;
	}

	public <D extends FileMetadata> D add(MultipartFile file, D fileInfo) {
		return add(file, fileInfo, null);
	}
	
	@SuppressWarnings("unchecked")
	public <D extends FileMetadata> D add(MultipartFile file, D fileInfo, String additionalPath) {
		FileMetadata filemeta = uploadFile(file, additionalPath);		
		try {
			copyInfo(filemeta, fileInfo);
			
			fileDao.add(fileInfo);
			return fileInfo;
		} catch (Exception e) {
			deleteFile(filemeta.getFullName());
			throw new FileUploadException("Could not upload file.", e);
		}
	}
	
	protected <D extends FileMetadata> void copyInfo(FileMetadata source, D target) {
		target.setLogicalName(source.getLogicalName());
		target.setPhysicalName(source.getPhysicalName());
		target.setLocation(source.getLocation());
		target.setExtension(source.getExtension());
		target.setSize(source.getSize());
	}
	
	@SuppressWarnings("unchecked")
	public <D extends FileMetadata> D get(Object parameters) {
		return (D)fileDao.get(parameters);
	}
	
	@SuppressWarnings("unchecked")
	public <D extends FileMetadata> void delete(Object parameters) {
		D temp = (D)fileDao.get(parameters);
		fileDao.delete(parameters);
		super.deleteFile(temp.getPhysicalFullPath());
	}

	@Override
	protected void initialize() {
		super.initialize();
		Assert.notNull(fileDao, "FileDao must be set.");
	}
}
