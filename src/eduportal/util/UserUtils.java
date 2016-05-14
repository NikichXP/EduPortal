package eduportal.util;

import java.security.*;
import javax.inject.*;
import javax.servlet.http.*;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AuthContainer;

public class UserUtils {
	
	@Inject private static AuthContainer auth;
	
	private static MessageDigest mDigest = null;
	static {
		try {
			mDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/** Encodes password with SHA-512 */
	public static String encodePass (String pass) {
		byte[] result = mDigest.digest(pass.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
	public static UserEntity getUserByCookie (HttpServletRequest req) {
		String token = null;
		for (Cookie c : req.getCookies()) {
			if (c.getName().equals("sesToken")) { //XXX: name of cookie can be changed!
				token = c.getValue();
				break;
			}
		}
		return (auth.getUser(token));
	}
	
}
