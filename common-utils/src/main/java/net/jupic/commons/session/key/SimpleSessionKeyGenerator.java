package net.jupic.commons.session.key;

import java.util.Random;

/**
 * @author chang jung pil
 *
 */
public class SimpleSessionKeyGenerator implements SessionKeyGenerator<String> {

	private int keyLength = 24;
	
	public SimpleSessionKeyGenerator() {
		this(24);
	}
	
	public SimpleSessionKeyGenerator(int keyLength) {
		this.keyLength = keyLength;
	}
	
	@Override
	public String generateKey() {
		return getRandomString(keyLength);
	}	
	
	private String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
         
        String[] chars = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0".split(",");
         
        for (int i = 0; i < length; i++) {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }
}
