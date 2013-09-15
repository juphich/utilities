package net.jupic.commons.http;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import net.jupic.commons.http.mock.Message;
import net.jupic.commons.http.mock.MockMessageHandler;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.junit.Test;


public class TestResponseHandlerTest {

	@Test
	public void testJsonHandler() throws ClientProtocolException, IOException {
		StringEntity entity = new StringEntity("code:code0/message:test-message", ContentType.create("text/plain", "UTF-8"));
		
		HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
		response.setEntity(entity);
		
		MockMessageHandler handler = new MockMessageHandler();
		Message message = handler.handleResponse(response);
		
		assertThat(message.getCode(), is("code0"));
		assertThat(message.getMessage(), is("test-message"));
	}
}
