package eduportal.util;

import java.security.*;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AccessSettings;
import eduportal.model.AuthContainer;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class UserUtils {
	
	public static void changePass (String acc, String newPass, String token) {
		AuthContainer.remove(token);
		UserEntity user = ofy().load().type(UserEntity.class).filter("mail", acc).first().now();
		user.setPass(newPass);
		ofy().save().entity(user);
	}
	
	public static final int CRYPTOLENGTH = 128;
	private static MessageDigest mDigest = null;
	static {
		try {
			mDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public static String encodePass (String pass) {
		byte[] result = mDigest.digest(pass.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
	
}
