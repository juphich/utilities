package net.jupic.commons.http;

import net.jupic.commons.http.context.DeleteRequestContext;
import net.jupic.commons.http.context.GetRequestContext;
import net.jupic.commons.http.context.HttpRequestContext;
import net.jupic.commons.http.context.PostRequestContext;
import net.jupic.commons.http.context.PutRequestContext;
import net.jupic.commons.http.exception.RequesterCreationException;
import net.jupic.commons.http.exception.UnsupportedRequestException;


public class HttpRequesterFactory {

	public static HttpRequester getRequester(HttpRequestContext context) 
			throws RequesterCreationException, UnsupportedRequestException {
		
		if (context instanceof GetRequestContext) {
			return new GetRequester((GetRequestContext) context);
		} else if (context instanceof PostRequestContext) {
			return new PostRequester((PostRequestContext) context);
		} else if (context instanceof PutRequestContext) {
			throw new UnsupportedRequestException("It doesn't support yet. - (put method)");
		} else if (context instanceof DeleteRequestContext) {
			throw new UnsupportedRequestException("It doesn't support yet. - (delete method)");
		}
		
		throw new UnsupportedRequestException("unknwon request method type.... : " + context.getClass());
	}
}