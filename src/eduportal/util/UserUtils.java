package eduportal.util;

import java.security.*;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AccessSettings;

public class UserUtils {
	
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
	
	
	
	public UserEntity setModerator (UserEntity u) {
		u.getPermission().setCategory(AccessSettings.MIN_MODERATOR_LVL);
		return u;
	}
	
	/**
	 * Here - other methods - adding country etc
	 */
}
