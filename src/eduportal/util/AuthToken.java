package eduportal.util;

public class AuthToken {
	
	private String sessionId;
	private long timeout;
	
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
	
}
