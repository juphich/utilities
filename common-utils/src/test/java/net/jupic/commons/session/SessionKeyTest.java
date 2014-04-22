package net.jupic.commons.session;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import net.jupic.commons.session.key.SessionKeyGenerator;
import net.jupic.commons.session.key.TextBaseKeyGenerator;
import net.jupic.commons.session.key.TimeSessionKeyGenerator;

import org.junit.Test;

public class SessionKeyTest {

	@Test
	public void testSessionKey() {
		SessionKeyGenerator<String> keyGen = new TimeSessionKeyGenerator();
		String key = keyGen.generateKey();
		
		assertNotNull(key);
	}
	
	@Test
	public void testTextKey() {
		SessionKeyGenerator<String> keyGen = new TextBaseKeyGenerator("test");
		String key = keyGen.generateKey();
		
		System.out.println(key);
		
		assertNotNull(key);
		assertThat(key.length(), is(40));
	}
	
	@Test
	public void testTextKeyMD5() {
		SessionKeyGenerator<String> keyGen = new TextBaseKeyGenerator("test", "MD5");
		String key = keyGen.generateKey();
		
		System.out.println(key);
		
		assertNotNull(key);
		assertThat(key.length(), is(32));
	}
}
