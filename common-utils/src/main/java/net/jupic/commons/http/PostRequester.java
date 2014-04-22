package net.jupic.commons.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jupic.commons.http.context.PostRequestContext;
import net.jupic.commons.http.exception.HttpRequestException;
import net.jupic.commons.http.exception.RequesterCreationException;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class PostRequester extends AbstractHttpRequester {

	private Map<String, String[]> formParameters;
	
	PostRequester(PostRequestContext context) throws RequesterCreationException {
		super(context);
		this.formParameters = context.getFormPrameters();
	}

	@Override
	public <R> R request(ResponseHandler<R> handler) throws HttpRequestException, HttpResponseException {
		HttpClient client = new DefaultHttpClient();
		
		super.setParameters(client.getParams());
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		if (formParameters != null && !formParameters.isEmpty()) {
			for (Entry<String, String[]> entry : formParameters.entrySet()) {
				String[] values = entry.getValue();
				if (values != null && values.length > 0) {
					for (String value : values) {
						params.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
			}
		}
		
		try {
			HttpPost httpPost = new HttpPost(getRequestUri());
			this.applyHeaders(httpPost);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			
			if (log.isDebugEnabled()) {
				log.debug("http request... : " + httpPost.getURI());
			}
			
			return client.execute(httpPost, handler);
		} catch (UnsupportedEncodingException e) {
			throw new HttpRequestException("request failed. (setting form parameters...) : " + e.getMessage());
		} catch (HttpResponseException response) {
			throw response;
		} catch (IOException e) {
			throw new HttpRequestException("request failed. : " + e.getMessage() , e);
		}  finally {
			client.getConnectionManager().shutdown();
		}
	}
}
