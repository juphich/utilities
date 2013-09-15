package net.jupic.commons.http.context;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.utils.URIBuilder;


public class GetRequestContext extends AbstractRequestContext<GetRequestContext> {

	private URI uri;
	private Map<String, List<String>> queryStrings;
	
	public GetRequestContext(String uri) throws URISyntaxException {
		this(new URI(uri));
	}
	
	public GetRequestContext(URI uri) {
		this.uri = uri;
		this.queryStrings = new HashMap<String, List<String>>();
	}
	
	public GetRequestContext addQueryString(String name, String value) {
		if (queryStrings.containsKey(name)) {
			queryStrings.get(name).add(value);
		} else {
			List<String> param = new ArrayList<String>(1);
			param.add(value);
			queryStrings.put(name, param);
		}
		
		return this;
	}
	
	public Map<String, String[]> getQueryStrings() {
		Map<String, List<String>> queryMap = extractQueryFromUri();
		
		//merge
		for (Entry<String, List<String>> entry : queryStrings.entrySet()) {
			if (queryMap.containsKey(entry.getKey())) {
				queryMap.get(entry.getKey()).addAll(entry.getValue());
			} else {
				queryMap.put(entry.getKey(), entry.getValue());
			}
		}
		
		Map<String, String[]> returnMap = new HashMap<String, String[]>();
		
		for(Entry<String, List<String>> entry : queryMap.entrySet()) {
			returnMap.put(entry.getKey(), entry.getValue().toArray(new String[]{}));
		}
		return returnMap;
	}
	
	private Map<String, List<String>> extractQueryFromUri() {
		Map<String, List<String>> queries = new HashMap<String, List<String>>();
		
		String query = uri.getQuery();
		if (query != null) {
			query = query.replace("&amp;", "&");
			String[] params = query.split("[;&]");
			for (String param : params) {
				String[] pair = param.split("=");
				if (pair.length == 2) {
					if (queries.containsKey(pair[0])) {
						queries.get(pair[0]).add(pair[1]);
					} else {
						List<String> values = new ArrayList<String>(5);
						values.add(pair[1]);
						queries.put(pair[0], values);
					}
				}
			}
		}
		
		return queries;
	}
	
	@Override
	public URI getRequestUri() throws URISyntaxException {
		URIBuilder builder = new URIBuilder(this.uri);
		for(Entry<String, List<String>> entry : queryStrings.entrySet()) {
			for (String value : entry.getValue()) {
				builder.addParameter(entry.getKey(), value);
			}
		}
		
		return builder.build();
	}
}
