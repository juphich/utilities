package net.jupic.commons.http;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import net.jupic.commons.http.context.HttpRequestContext;
import net.jupic.commons.http.exception.HttpRequestException;
import net.jupic.commons.http.exception.RequesterCreationException;

import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractHttpRequester implements HttpRequester {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private URI requestUri;
	private Map<String, String> headers;
	private Map<String, String> cookies;
	
	private Map<String, Object> httpParams;
	
	AbstractHttpRequester(HttpRequestContext context) throws RequesterCreationException {
		try {
			this.requestUri = context.getRequestUri();
			this.headers = context.getHeaders();
			this.cookies = context.getCookies();
			this.httpParams = context.getPrameters();
		} catch (Exception e) {
			throw new RequesterCreationException("requester creation failed!! - cause : " + e.getMessage(), e);
		}
	}
	
	protected URI getRequestUri() {
		return requestUri;
	}
	
	protected void addHeader(String name, String value) {
		headers.put(name, value);
	}
	
	protected void addCookie(String name, String value) {
		cookies.put(name, value);
	}
	
	protected void setParameters(HttpParams parameters) {
		if (httpParams != null) {
			for (Entry<String, Object> entry : httpParams.entrySet()) {
				parameters.setParameter(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public String request() throws HttpRequestException, HttpResponseException {
		return request(new BasicResponseHandler());
	}
}
