package net.jupic.commons.http.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PostRequestContext extends AbstractRequestContext<PostRequestContext> {

	private URI requestUri;
	private Map<String, List<String>> formParameters;
	
	public PostRequestContext(String requestUri) throws URISyntaxException {
		this(new URI(requestUri));
	}
	
	public PostRequestContext(URI requestUri) {
		this.requestUri = requestUri;
		this.formParameters = new HashMap<String, List<String>>();
	}
	
	public PostRequestContext addFormParameter(String name, String value) {
		if (formParameters.containsKey(name)) {
			formParameters.get(name).add(value);
		} else {
			List<String> values = new ArrayList<String>(5);
			values.add(value);
			formParameters.put(name, values);			
		}
		return this;
	}

	@Override
	public URI getRequestUri() {
		return requestUri;
	}

	public Map<String, String[]> getFormPrameters() {
		Map<String, String[]> returnMap = new HashMap<String, String[]>();
		
		for(Entry<String, List<String>> entry : formParameters.entrySet()) {
			returnMap.put(entry.getKey(), entry.getValue().toArray(new String[]{}));
		}
		return returnMap;
	}
}
