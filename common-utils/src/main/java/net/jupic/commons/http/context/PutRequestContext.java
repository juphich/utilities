package net.jupic.commons.http.context;

import java.net.URI;

public class PutRequestContext extends AbstractRequestContext<PutRequestContext> {

	@Override
	public URI getRequestUri() {
		throw new UnsupportedOperationException();
	}
}
