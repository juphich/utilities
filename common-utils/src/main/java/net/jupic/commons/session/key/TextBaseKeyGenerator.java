package net.jupic.commons.session.key;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import net.jupic.commons.session.exception.KeyGenerationException;

public class TextBaseKeyGenerator implements SessionKeyGenerator<String> {

	private String baseText;
	private String algorithm = "SHA1";
	
	public TextBaseKeyGenerator(String base) {
		this(base, "SHA1");
	}
	
	public TextBaseKeyGenerator(String base, String algorithm) {
		this.baseText = base;
		this.algorithm = algorithm;
	}
	
	@Override
	public String generateKey() {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] digest = md.digest(baseText.getBytes());
			
			return new String(Hex.encodeHex(digest));
		} catch (NoSuchAlgorithmException e) {
			throw new KeyGenerationException(e);
		}
	}
}
