package net.jupic.spring.web.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jupic.spring.file.FileMetadata;
import net.jupic.spring.web.exception.UndefinedSizeException;
import net.jupic.spring.web.model.FileModelMeta;
import net.jupic.spring.web.view.exception.InvalidatedModelTypeException;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.AbstractView;


public class DownloadView extends AbstractView {

	private Class<? extends FileMetadata> modelType = FileMetadata.class;
	private String modelKey = "file";
	
	public Class<?> getModelType() {
		return modelType;
	}

	public String getModelKey() {
		return modelKey;
	}

	public void setModelType(Class<? extends FileMetadata> modelType) {
		this.modelType = modelType;
	}

	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
										   HttpServletRequest  request,
										   HttpServletResponse response) throws Exception {
		FileModelMeta source = getFileSource(model);
		
		response.setHeader("Content-type", "application/octet-stream");
		response.setHeader("Content-Disposition", getDisposition(request, source.getFilename()));
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "private");
		try {
			response.setHeader("Content-Length", String.valueOf(source.getSize()));
		} catch (UndefinedSizeException e) { }
		
		FileCopyUtils.copy(source.getInputStream(), response.getOutputStream());		
	}
	
	private String getDisposition(HttpServletRequest request, String name) throws UnsupportedEncodingException {
		String disposition = "fileName = " + URLEncoder.encode(name, "UTF-8") + ";";
		
		if (request.getHeader("User-Agent").indexOf("MSIE") > -1) {
			disposition = "attachment; " + disposition;
		}
		return disposition;
	}

	private FileModelMeta getFileSource(Map<String, Object> model) {
		if (model.isEmpty()) {
			throw new IllegalArgumentException("There isn't any view model.");
		}
		
		Collection<Object> models = model.values();
		FileModelMeta result = null;
		int count = 0;
		for (Object source : models) {
			if (isValidSource(source)) {
				if (count == 0) {
					result = (source instanceof FileModelMeta) ? (FileModelMeta)source : new InnerModelMeta(source);
					count++;					
				} else {
					throw new IllegalArgumentException("Too many view models... ");					
				}
			}
		}
		
		if (result == null) {
			throw new IllegalArgumentException("There isn't any view model.");
		}
		
		return result;
	}
	
	private boolean isValidSource(Object source) {
		return (source instanceof MultipartFile) 
				|| (source instanceof File)
				|| (source instanceof FileModelMeta)
				|| modelType.isAssignableFrom(source.getClass()); 
	}
	
	private class InnerModelMeta implements FileModelMeta {
		private Object source;
		
		InnerModelMeta(Object source) {
			if (!(source instanceof File) 
					&& !(source instanceof MultipartFile)
					&& !modelType.isAssignableFrom(source.getClass())) {
				throw new InvalidatedModelTypeException("It's a invalidated view model.", source);
			}
			
			this.source = source;
		}
		
		@Override
		public String getFilename() {
			if (source instanceof MultipartFile) {
				return ((MultipartFile) source).getOriginalFilename();
			} else if (source instanceof File) {
				return ((File)source).getName();
			} else {
				return ((FileMetadata) source).getLogicalName();
			}
		}
		
		@Override
		public long getSize() {
			if (source instanceof MultipartFile) {
				return ((MultipartFile) source).getSize();
			} else if (source instanceof File) {
				return ((File)source).length();
			} else {
				return ((FileMetadata) source).getSize();
			}
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			if (source instanceof MultipartFile) {
				return ((MultipartFile) source).getInputStream();
			} else if (source instanceof File) {
				return new FileInputStream((File) source);
			} else {
				return new FileInputStream(((FileMetadata)source).getPhysicalFullPath());
			}
		}
	}
}
