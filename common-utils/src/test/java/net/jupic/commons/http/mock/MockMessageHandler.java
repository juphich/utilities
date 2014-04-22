package net.jupic.commons.http.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.jupic.commons.http.handler.AbstractMappingHandler;

import org.apache.http.HttpResponse;

public class MockMessageHandler extends AbstractMappingHandler<Message> {

	@Override
	public boolean isError(HttpResponse response) {
		return response.getStatusLine().getStatusCode() >= 300;
	}

	@Override
	public Message parse(InputStream source) {
		
		try {
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(source));
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			
			String[] result = buffer.toString().split("/");
			Message message = new Message(result[0].substring(result[0].indexOf(":")+1),
										  result[1].substring(result[1].indexOf(":")+1));
			
			return message;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
