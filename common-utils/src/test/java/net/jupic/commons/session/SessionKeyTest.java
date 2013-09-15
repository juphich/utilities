package net.jupic.commons.session;

import static org.junit.Assert.assertNotNull;

import net.jupic.commons.session.key.SessionKeyGenerator;
import net.jupic.commons.session.key.TimeSessionKeyGenerator;

import org.junit.Test;


public class SessionKeyTest {

	@Test
	public void testSessionKey() {
		SessionKeyGenerator<String> keyGen = new TimeSessionKeyGenerator();
		String key = keyGen.generateKey();
		
		assertNotNull(key);
	}
}
