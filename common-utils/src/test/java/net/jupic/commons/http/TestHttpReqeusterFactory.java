package net.jupic.commons.http;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Map;

import net.jupic.commons.http.HttpRequester;
import net.jupic.commons.http.HttpRequesterFactory;
import net.jupic.commons.http.PostRequester;
import net.jupic.commons.http.context.GetRequestContext;
import net.jupic.commons.http.context.HttpRequestContext;
import net.jupic.commons.http.context.PostRequestContext;

import org.junit.Test;


public class TestHttpReqeusterFactory {

	@Test
	public void testHttpRequesContext() throws URISyntaxException {
		HttpRequestContext context = new GetRequestContext("http://www.estgames.com/api?param=test1&amp;param=test2");
		Map<String, String[]> queries = ((GetRequestContext) context).getQueryStrings();
		
		assertThat(queries.size(), is(1));
		assertArrayEquals(queries.get("param"), new String[] {"test1", "test2"});
	}
	
	@Test
	public void testHttpRequesterTest() throws URISyntaxException {
		HttpRequestContext context = 
				new PostRequestContext("https://logins.daum.net/accounts/checkauth")
						.setContentCharset("UTF-8")
						.setConnectionTimeout(1000)
						.addFormParameter("userip", "127.0.0.1")
						.addFormParameter("ipcheck", "true");
		
		HttpRequester requester = HttpRequesterFactory.getRequester(context);
		
		assertTrue(requester.getClass().equals(PostRequester.class));
	}
}
