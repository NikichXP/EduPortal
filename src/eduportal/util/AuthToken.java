package eduportal.util;

import eduportal.model.AccessSettings;

public class AuthToken {
	
	private String sessionId;
	private long timeout;
	private String accessLevel;
	
	public String getSessionId() {
		return sessionId;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public void setTimeoutTimestamp(long timeoutTimestamp) {
		this.timeout = timeoutTimestamp;
	}
	public String getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	public void putAccessLevelInt(int al) {
		this.accessLevel = ( (al == AccessSettings.MODERATOR_LEVEL) ? "MODERATOR" : (al == AccessSettings.ADMIN_LEVEL) ? "ADMIN" : "USER");
	}
	
	
}
