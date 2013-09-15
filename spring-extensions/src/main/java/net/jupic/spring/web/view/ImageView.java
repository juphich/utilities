package net.jupic.spring.web.view;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class ImageView extends AbstractView {

	private static Logger logger = LoggerFactory.getLogger(ImageView.class);
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
										   HttpServletRequest request, 
										   HttpServletResponse response) throws Exception {
		String fileName = null;
		FileInputStream is = null;
		try {
			File file = (File) model.get("image");
			fileName = file.getName();
			
			checkMimeType(file);
			
			response.setContentType("image/jpeg");
			response.setContentLength((int)file.length());
			
			is = new FileInputStream(file);
			FileCopyUtils.copy(is, response.getOutputStream());
			
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.error("catched image showing error", e);
			}
			
			logger.warn("could not show image. : {}", fileName);
		} finally {
			is.close();
		}
	}
	
	private String checkMimeType(File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		
		String mimeType = mimeTypesMap.getContentType(file);
		
		return mimeType;
	}

}
