/**
 * 
 */
package net.jupic.commons.http.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

/**
 * @author chang jung pil
 *
 */
public abstract class AbstractRequestContext<D extends HttpRequestContext> implements HttpRequestContext {
	
	private Map<String, String> headers = new HashMap<String, String>();
	private Map<String, String> cookies = new HashMap<String, String>();
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	public D addHeader(String name, String value) {
		headers.put(name, value);
		return (D)this;
	}
	
	@SuppressWarnings("unchecked")
	public D addCookie(String name, String value) {
		cookies.put(name, value);
		return (D) this;
	}
	
	@SuppressWarnings("unchecked")
	public D setConnectionTimeout(int timeout) {
		parameters.put(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
		return (D) this;
	}
	
	@SuppressWarnings("unchecked")
	public D setSocketTimeout(int timeout) {
		parameters.put(CoreConnectionPNames.SO_TIMEOUT, timeout);
		return (D) this;
	}
	
	@SuppressWarnings("unchecked")
	public D setProtocol(String protocol) {
		parameters.put(CoreProtocolPNames.PROTOCOL_VERSION, protocol);
		return (D) this;
	}
	
	@SuppressWarnings("unchecked")
	public D setContentCharset(String charset) {
		parameters.put(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);
		return (D) this;
	}
	
	@SuppressWarnings("unchecked")
	public D setElementCharset(String charset) {
		parameters.put(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, charset);
		return (D) this;
	}

	@Override
	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(headers);
	}

	@Override
	public Map<String, String> getCookies() {
		return Collections.unmodifiableMap(cookies);
	}
	
	@Override
	public Map<String, Object> getPrameters() {
		return Collections.unmodifiableMap(parameters);
	}
}
