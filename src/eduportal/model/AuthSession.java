package eduportal.model;

import eduportal.dao.entity.UserEntity;

class AuthSession {
	private UserEntity user;
	private long timeout;
	private int accessLevel;

	public AuthSession() {
	}

	public AuthSession(UserEntity user) {
		this.user = user;
		this.accessLevel = user.getAccessLevel();
		this.timeout = System.currentTimeMillis() + ((user.getAccessLevel() >= AccessSettings.MODERATOR_LEVEL)
				? AccessSettings.WORKER_SESSION_TIMEOUT : AccessSettings.USER_SESSION_TIMEOUT);
	}

	public AuthSession(UserEntity usr, long timeout2, int acclvl) {
		this.user = usr;
		this.timeout = timeout2;
		this.accessLevel = acclvl;
	}

	public UserEntity getUser() {
		return user;
	}

	public long getTimeout() {
		return timeout;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
}