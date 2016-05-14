package eduportal.model;

import java.util.*;

import com.google.api.server.spi.config.*;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.util.AuthToken;

@Singleton
public class AuthContainer {

	private HashMap<String, AuthSession> sessions = new HashMap<>();
	private HashSet<Long> users;
	
	private static AuthContainer instance = new AuthContainer();
	public static AuthContainer getInstance () {
		return instance;
	}
	private AuthContainer () {
		sessions = new HashMap<>();
		users = new HashSet<>();
	}

	public ArrayList<String> testMethod() {
		ArrayList<String> ret = new ArrayList<>();
		for (String s : sessions.keySet()) {
			ret.add("Session: " + s + "  ==  " + sessions.get(s).getUser());
			ret.add("Token_t: " + s + "  ==  "
					+ (((double) sessions.get(s).getTimeout() - System.currentTimeMillis()) / (1000 * 3600)));
			ret.add("Access : " + s + "  ==  " + sessions.get(s).getAccessLevel());
		}
		return ret;
	}

	/**
	 * Session timeout
	 */
	public static final long SESSION_TIME = 3600 * 1000; // 1h

	/**
	 * Authenticate user
	 * 
	 * @param login
	 *            - user's login
	 * @param pass
	 *            - user's pass
	 * @return String token if login and pass are true; null if bad credentials
	 */
	public AuthToken authenticate(String login, String pass) {
		System.out.println("Getting user creds");
		UserEntity user = UserDAO.get(login, pass);
		if (user == null) {
			return null;
		}
		if (users.contains(user.getId()) == false) {
			users.add(user.getId());
		} else {
			for (String s : sessions.keySet()) {
				if (sessions.get(s).getUser().getId() == user.getId()) {
					sessions.remove(s);
				}
			}
		}
		String token = UUID.randomUUID().toString();
		AuthSession session = new AuthSession(user);
		sessions.put(token, session);
		AuthToken ret = new AuthToken();
		ret.setSessionId(token);
		ret.setTimeoutTimestamp(System.currentTimeMillis() + SESSION_TIME);
		return ret;
	}

	public boolean checkReq(String token, int acclvl) {
		if (sessions.get(token).getAccessLevel() >= acclvl) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return user if token is valid & session not over
	 * 
	 * @param token
	 *            - session id
	 * @return - User
	 */
	public UserEntity getUser(String token) {
		if (token == null) return null;
		UserEntity u = null;
		try {
			u = sessions.get(token).getUser();
		} catch (Exception e) {
			return null;
		}
		if (u != null) {
			if (sessions.get(token).getTimeout() > System.currentTimeMillis()) {
				return u;
			} else {
				sessions.remove(token);
				users.remove(u.getId());
			}
		}
		return null;
	}

	public int getAccessGroup(String token) {
		return sessions.get(token).getAccessLevel();
	}

	/**
	 * Reset session timeout
	 * 
	 * @param token
	 *            - sessionID to update
	 */
	public void updateTimeout(String token) {
		if (sessions.get(token).getTimeout() < System.currentTimeMillis()) {
			sessions.get(token).setTimeout(System.currentTimeMillis() + SESSION_TIME);
		}
	}

}
