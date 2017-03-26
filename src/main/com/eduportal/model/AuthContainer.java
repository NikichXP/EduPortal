package com.eduportal.model;

import com.eduportal.entity.Employee;
import com.eduportal.entity.UserEntity;
import com.eduportal.util.AuthToken;
import com.eduportal.AppLoader;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.AuthSession;
import com.eduportal.repo.AuthRepository;

import java.util.*;

public class AuthContainer {

	private static final HashMap<String, AuthSession> sessions = new HashMap<>();

	private static AuthRepository authRepo = AppLoader.get(AuthRepository.class);

	public static AuthToken requireAuth (Employee emp, UserEntity target) {
		if (!AccessLogic.canEditUser(emp, target)) {
			return null;
		}
		return auth(target);
	}
	
	public static AuthToken authenticate(String login, String pass) {
		UserEntity user = UserDAO.get(login, pass);
		if (user == null) {
			return new AuthToken().setAccessLevel("FAIL");
		}
		return auth(user);
	}
	
	private static AuthToken auth(UserEntity user) {
		String token = UUID.randomUUID().toString();
		AuthSession session = new AuthSession(user);
		synchronized (sessions) {
			put(token, session);
		}
		AuthToken ret = new AuthToken();
		ret.setSessionId(token);
		ret.setTimeoutTimestamp(session.getTimeout());
		ret.putAccessLevelInt(session.getAccessLevel());
		// TODO Re-do this
		if (session.getAccessLevel() > 10) {
			if (!((Employee) user).getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
				ret.putAccessLevelInt(-70);
			}
		}
		return ret;
	}

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
				u = get(token).user();
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

	public static Employee getEmp(String token) {
		UserEntity user = getUser(token);
		if (user == null) {
			return null;
		}
		if (user instanceof Employee) {
			return (Employee) user;
		} else {
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
	
	private static Set<String> keySet() {
		Set<String> ret = sessions.keySet();
		return ret;
	}

	private static AuthSession get(String key) {
		if (sessions.get(key) != null) {
			return sessions.get(key);
		}
		AuthSession ret = authRepo.findOne(key);
		if (ret == null) {
			return null;
		}
		if (ret.getTimeout() < System.currentTimeMillis()) {
			authRepo.getOutdated(System.currentTimeMillis()).forEach(AuthContainer::remove);
			return null;
		}
		sessions.put(key, ret);
		return ret;
	}

	private static void remove(AuthSession authSession) {
		remove(authSession.getToken());
	}

	private static void put(String key, AuthSession value) {
		value.setToken(key);
		sessions.put(key, value);
		authRepo.save(value);
	}

	public static void remove(String token) {
		try {
			sessions.remove(token);
		} catch (Exception e) {
		}
		authRepo.delete(token);
	}

	// CUT BELOW
	public static ArrayList<String> testMethod() {
		synchronized (sessions) {
			ArrayList<String> ret = new ArrayList<>();
			for (String s : keySet()) {
				ret.add("Session: " + s + "  ==  " + sessions.get(s).user());
				ret.add("Token_t: " + s + "  ==  "
						+ (((double) sessions.get(s).getTimeout() - System.currentTimeMillis()) / (1000 * 3600)));
				ret.add("Access : " + s + "  ==  " + sessions.get(s).user().getClass().getName());
			}
			return ret;
		}
	}

	public static void reinit(List<AuthSession> l) {
		for (AuthSession as : l) {
			sessions.put(as.getToken(), as);
		}
	}
}
