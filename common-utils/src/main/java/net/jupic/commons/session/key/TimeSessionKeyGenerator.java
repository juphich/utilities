package net.jupic.commons.session.key;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import net.jupic.commons.utils.DateFormatUtils;

import org.apache.commons.codec.binary.Base64;


public class TimeSessionKeyGenerator implements SessionKeyGenerator<String> {

	private DateFormatUtils dateUtil = new DateFormatUtils();
	
	@Override
	public String generateKey() {
		String date = dateUtil.formatDate(new Date(System.currentTimeMillis()), "YYYYMMddhhmmss");
		String rndStr = getRandomString(30);
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] digest = md.digest((rndStr + date).getBytes());
			
			return Base64.encodeBase64String(digest).replace("=", "E").toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
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
