package net.jupic.commons.http.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public interface HttpRequestContext {
	URI getRequestUri() throws URISyntaxException;
	Map<String, Object> getPrameters();
	Map<String, String> getHeaders();
	Map<String, String> getCookies();
}
