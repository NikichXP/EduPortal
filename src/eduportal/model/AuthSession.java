package eduportal.model;

import eduportal.dao.entity.UserEntity;

class AuthSession {
	private UserEntity user;
	private long timeout;
	private int accessLevel;

	public AuthSession() {
	}

	public AuthSession(UserEntity u) {
		this.user = u;
		this.accessLevel = u.getAccessGroup();
		this.timeout = System.currentTimeMillis() + AuthContainer.SESSION_TIME;
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