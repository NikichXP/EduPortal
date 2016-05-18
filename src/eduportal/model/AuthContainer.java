package eduportal.model;

import java.util.*;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.util.AuthToken;

public class AuthContainer {

	private static HashMap<String, AuthSession> sessions;
	private static HashSet<Long> users;

	static {
		sessions = new HashMap<>();
		users = new HashSet<>();
		String token = authenticate("admin", "pass").getSessionId();
		sessions.put("aaaa", sessions.get(token));
	}

	public static ArrayList<String> testMethod() {
		ArrayList<String> ret = new ArrayList<>();
		for (String s : sessions.keySet()) {
			ret.add("Session: " + s + "  ==  " + sessions.get(s).getUser());
			ret.add("Token_t: " + s + "  ==  "
					+ (((double) sessions.get(s).getTimeout() - System.currentTimeMillis()) / (1000 * 3600)));
			ret.add("Access : " + s + "  ==  " + sessions.get(s).getUser().getAccessLevel());
		}
		return ret;
	}

	/**
	 * Authenticate user
	 * 
	 * @param login
	 *            - user's login
	 * @param pass
	 *            - user's pass
	 * @return String token if login and pass are true; null if bad credentials
	 */
	public static AuthToken authenticate(String login, String pass) {
		UserEntity user = UserDAO.get(login, pass);
		if (user == null) {
			return null;
		}
		if (user.getAccessLevel() >= AccessSettings.MODERATOR_LEVEL) {
			if (!AccessSettings.ALLOW_WORKER_MULTISESSIONS) {
				if (users.contains(user.getId()) == false) {
					users.add(user.getId());
				} else {
					for (String s : sessions.keySet()) {
						if (sessions.get(s).getUser().getId() == user.getId()) {
							sessions.remove(s);
						}
					}
				}
			}
		} else {
			if (!AccessSettings.ALLOW_USER_MULTISESSIONS) {
				if (users.contains(user.getId()) == false) {
					users.add(user.getId());
				} else {
					for (String s : sessions.keySet()) {
						if (sessions.get(s).getUser().getId() == user.getId()) {
							sessions.remove(s);
						}
					}
				}
			}
		}
		String token = UUID.randomUUID().toString();
		AuthSession session = new AuthSession(user);
		sessions.put(token, session);
		AuthToken ret = new AuthToken();
		ret.setSessionId(token);
		ret.setTimeoutTimestamp(System.currentTimeMillis() + ((user.getAccessLevel() >= AccessSettings.MODERATOR_LEVEL)
				? AccessSettings.WORKER_SESSION_TIMEOUT : AccessSettings.USER_SESSION_TIMEOUT));
		return ret;
	}

	public static boolean checkReq(String token, int acclvl) {
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
	public static UserEntity getUser(String token) {
		if (token == null)
			return null;
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

	public static int getAccessGroup(String token) {
		return sessions.get(token).getAccessLevel();
	}

	/**
	 * Reset session timeout
	 * 
	 * @param token
	 *            - sessionID to update
	 */
	public static void updateTimeout(String token) {
		if (sessions.get(token).getTimeout() < System.currentTimeMillis()) {
			sessions.get(token).setTimeout(sessions.get(token).getTimeout() + 3600_000);
		}
	}

	public static boolean checkToken(String token) {
		if (token == null) {
			return false;
		}
		if (sessions.get(token) == null) {
			return false;
		}
		if (sessions.get(token).getTimeout() <= System.currentTimeMillis()) {
			return false;
		}
		return true;
	}

}
