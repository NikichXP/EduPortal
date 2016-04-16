package eduportal.model;

import java.util.HashMap;
import java.util.UUID;

import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;

public class AuthContainer {
	
	private static HashMap<String, UserEntity> currentSession = new HashMap();
	private static HashMap<String, Long> tokenLimitTime = new HashMap();
	
	/**
	 * Session timeout
	 */
	public static final long SESSION_TIME = 3600 * 1000; //1h
	
	
	/**
	 * Authenticate user
	 * @param login - user's login
	 * @param pass - user's pass
	 * @return String token if login and pass are true; null if bad credentials
	 */
	public static String auth (String login, String pass) {
		UserEntity user = UserDAO.get(login, pass);
		if (user == null) {
			return null;
		}
		String token = UUID.randomUUID().toString();
		currentSession.put(token, user);
		tokenLimitTime.put(token, System.currentTimeMillis() + SESSION_TIME);
		return token;
	}
	
	/**
	 * Reset session timeout
	 * @param token - sessionID to update
	 */
	public static void updateTimeout (String token) {
		tokenLimitTime.put(token, System.currentTimeMillis() + SESSION_TIME);
	}
	
}
