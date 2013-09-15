package net.jupic.commons.http.handler;

import java.io.IOException;
import java.io.InputStream;

import net.jupic.commons.http.exception.HttpResponseHandleException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractMappingHandler<T> implements ResponseHandler<T> {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private boolean obtainErrorMessage = false;
	
	public void setObtainErrorMessage(boolean obtainErrorMessage) {
		this.obtainErrorMessage = obtainErrorMessage;
	}
	
	public abstract boolean isError(HttpResponse response);

	public abstract T parse(InputStream source) throws IOException;
	
	protected void processError(HttpResponse response) throws 
			IOException, HttpResponseException, HttpResponseHandleException {
		
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		
		Object errorMessage = null;
		if (obtainErrorMessage) {
			errorMessage = obtainErrorMessage(entity.getContent());
		}
		EntityUtils.consume(entity);
		
		if (errorMessage != null) {
			throw new HttpResponseHandleException(statusLine.getStatusCode(), statusLine.getReasonPhrase(), errorMessage);
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());			
		}
	}
	
	protected Object obtainErrorMessage(InputStream source) throws IOException {
		return null;
	}
	
	@Override
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		HttpEntity entity = response.getEntity();
		
		if (isError(response)) {
			processError(response);			
        }
		
		T message = parse(entity.getContent());
		EntityUtils.consume(entity);
		
		return message;
	}
}
