package eduportal.model;

import java.util.*;
import java.util.concurrent.Future;
import java.util.logging.Level;
import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.util.AuthToken;
import com.google.appengine.api.memcache.*;

public class AuthContainer {

	private static HashMap<String, AuthSession> sessions = new HashMap<>();
	private static AsyncMemcacheService cache;
	
	static {
		cache = MemcacheServiceFactory.getAsyncMemcacheService();
		cache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	}
	
	private static Set<String> keySet() {
		Set<String> ret = sessions.keySet();
		return ret;
	}
	
	private static AuthSession get(String key) {
		if (sessions.get(key) != null) {
			return sessions.get(key);
		}
		try {
			Future<Object> futureValue = cache.get(key);
			AuthSession session = (AuthSession) futureValue.get();
			if (session != null) {
				return session;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	private static void put(String key, AuthSession value) {
		sessions.put(key, value);
		cache.put(key, value);
	}

	private static void remove(String token) {
		try {
			sessions.remove(token);
		} catch (Exception e) {
		}
		cache.delete(token);
	}

	public static AuthToken authenticate(String login, String pass) {
		synchronized (sessions) {
			UserEntity user = UserDAO.get(login, pass);
			if (user == null) {
				return null;
			}
			String token = UUID.randomUUID().toString();
			AuthSession session = new AuthSession(user);
			put(token, session);
			AuthToken ret = new AuthToken();
			ret.setSessionId(token);
			ret.setTimeoutTimestamp(session.getTimeout());
			return ret;
		}
	}

	// BELOW IS FOR UNCACHED VERS

	// /**
	// * Authenticate user
	// *
	// * @param login
	// * - user's login
	// * @param pass
	// * - user's pass
	// * @return String token if login and pass are true; null if bad
	// credentials
	// */
	// public static AuthToken authenticate(String login, String pass) {
	// synchronized (sessions) {
	// UserEntity user = UserDAO.get(login, pass);
	// if (user == null) {
	// return null;
	// }
	// if (user.getAccessLevel() >= AccessSettings.MODERATOR_LEVEL) {
	// if (!AccessSettings.ALLOW_WORKER_MULTISESSIONS) {
	// if (users.contains(user.getId()) == false) {
	// users.add(user.getId());
	// } else {
	// for (String s : sessions.keySet()) {
	// if (sessions.get(s).getUser().getId() == user.getId()) {
	// sessions.remove(s);
	// }
	// }
	// }
	// }
	// } else {
	// if (!AccessSettings.ALLOW_USER_MULTISESSIONS) {
	// if (users.contains(user.getId()) == false) {
	// users.add(user.getId());
	// } else {
	// for (String s : sessions.keySet()) {
	// if (sessions.get(s).getUser().getId() == user.getId()) {
	// sessions.remove(s);
	// }
	// }
	// }
	// }
	// }
	// String token = UUID.randomUUID().toString();
	// AuthSession session = new AuthSession(user);
	// sessions.put(token, session);
	// AuthToken ret = new AuthToken();
	// ret.setSessionId(token);
	// ret.setTimeoutTimestamp(
	// System.currentTimeMillis() + ((user.getAccessLevel() >=
	// AccessSettings.MODERATOR_LEVEL)
	// ? AccessSettings.WORKER_SESSION_TIMEOUT :
	// AccessSettings.USER_SESSION_TIMEOUT));
	// return ret;
	// }
	// }

	public static boolean checkReq(String token, int acclvl) {
		synchronized (sessions) {
			if (get(token).getAccessLevel() >= acclvl) {
				return true;
			} else {
				return false;
			}
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
		synchronized (sessions) {
			if (token == null) {
				return null;
			}
			UserEntity u = null;
			try {
				u = get(token).getUser();
			} catch (Exception e) {
				return null;
			}
			if (u != null) {
				if (get(token).getTimeout() > System.currentTimeMillis()) {
					return u;
				} else {
					remove(token);
				}
			}
			return null;
		}
	}

	public static int getAccessGroup(String token) {
		synchronized (sessions) {
			return get(token).getAccessLevel();
		}
	}

	/**
	 * Reset session timeout
	 * 
	 * @param token
	 *            - sessionID to update
	 */
	public static void updateTimeout(String token) {
		synchronized (sessions) {
			if (get(token).getTimeout() < System.currentTimeMillis()) {
				get(token).setTimeout(get(token).getTimeout() + 3600_000);
			}
		}
	}

	public static boolean checkToken(String token) {
		synchronized (sessions) {
			if (token == null) {
				return false;
			}
			if (get(token) == null) {
				return false;
			}
			if (get(token).getTimeout() <= System.currentTimeMillis()) {
				return false;
			}
			return true;
		}
	}

	// CUT BELOW
	public static ArrayList<String> testMethod() {
		synchronized (sessions) {
			ArrayList<String> ret = new ArrayList<>();
			for (String s : keySet()) {
				ret.add("Session: " + s + "  ==  " + sessions.get(s).getUser());
				ret.add("Token_t: " + s + "  ==  "
						+ (((double) sessions.get(s).getTimeout() - System.currentTimeMillis()) / (1000 * 3600)));
				ret.add("Access : " + s + "  ==  " + sessions.get(s).getUser().getAccessLevel());
			}
			return ret;
		}
	}
}
