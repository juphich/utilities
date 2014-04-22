package net.jupic.commons.http.context;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jupic.commons.exception.NotAPojoException;
import net.jupic.commons.utils.ReflectionUtils;

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
	
	public PostRequestContext addFormParameter(String name, Object value) {
		String textValue = null;
		if (value instanceof String) {
			textValue = (String)value;
		} else if (value instanceof Date) {
			textValue = String.valueOf(((Date) value).getTime());
		} else if (value instanceof Serializable) {
			textValue = value.toString();
		}
		
		return addFormParameterText(name, textValue);
	}
	
	private PostRequestContext addFormParameterText(String name, String value) {
		if (value == null || value.isEmpty()) {
			return this;
		}
		
		if (formParameters.containsKey(name)) {
			formParameters.get(name).add(value);
		} else {
			List<String> values = new ArrayList<String>(5);
			values.add(value);
			formParameters.put(name, values);			
		}
		return this;
	}
	
	public PostRequestContext addFormParameters(Object parameters) {
		try {
			return addFormParameters(ReflectionUtils.asMap(parameters));
		} catch (NotAPojoException e) {
			throw new IllegalArgumentException("This parameter object is not a applicable type..!! [Map<String, Object> or Pojo (is not a Collection, array)]");
		}
	}
	
	public PostRequestContext addFormParameters(Map<String, Object> parameters) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			this.addFormParameter(entry.getKey(), entry.getValue());
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
