package net.jupic.commons.http;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import net.jupic.commons.http.context.GetRequestContext;
import net.jupic.commons.http.context.HttpRequestContext;
import net.jupic.commons.http.context.PostRequestContext;
import net.jupic.commons.http.mock.Message;

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
	public void testHttpRequestContextForMap() throws URISyntaxException {
		Map<String, Object> queries = new HashMap<String, Object>();
		queries.put("param3", "test param");
		queries.put("param", "test3");
		
		HttpRequestContext context = new GetRequestContext("http://www.estgames.com/api?param=test1&amp;param=test2")
			.addQueries(queries);
		
		Map<String, String[]> params = ((GetRequestContext) context).getQueryStrings();
		assertThat(params.size(), is(2));
		assertArrayEquals(params.get("param"), new String[] {"test1", "test2", "test3"});
	}
	
	@Test
	public void testHttpRequestContextForPojo() throws URISyntaxException {
		Message message = new Message("success", "test message");
		
		HttpRequestContext context = new GetRequestContext("http://www.estgames.com/api?param=test1&amp;param=test2")
			.addQueries(message);
		
		Map<String, String[]> queries = ((GetRequestContext) context).getQueryStrings();
		assertThat(queries.size(), is(3));
		assertArrayEquals(queries.get("code"), new String[] {"success"});
		assertArrayEquals(queries.get("message"), new String[] {"test message"});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testHttpRequestContextFail() throws URISyntaxException {
		new GetRequestContext("http://www.estgames.com/api")
			.addQueries(new Object[0])
			.getQueryStrings();
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
