package com.eduportal.util;

import com.eduportal.entity.UserEntity;
import com.eduportal.model.AuthContainer;
import com.eduportal.repo.UserRepository;
import com.eduportal.AppLoader;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserUtils {

	private static UserRepository userRepo = AppLoader.get(UserRepository.class);
	
	public static void changePass (String mail, String newPass, String token) {
		AuthContainer.remove(token);
		UserEntity user = userRepo.findByMail(mail);
		user.setPass(newPass);
		userRepo.save(user);
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
