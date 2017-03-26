package com.eduportal.entity;

import com.eduportal.dao.UserDAO;
import com.eduportal.model.AccessSettings;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class AuthSession implements Serializable {
	private static final long serialVersionUID = -3213250756514680668L;
	@Id
	private String token;
	private String userid;
	private long timeout;
	private int accessLevel;
	
	@Deprecated
	public AuthSession() {
	}

	public AuthSession(UserEntity user) {
		this.userid = user.getId();
		this.accessLevel = (user instanceof Employee) ? ((Employee)user).getAccessLevel() : 0;
		this.timeout = System.currentTimeMillis() + ((this.accessLevel >= AccessSettings.MODERATOR_LEVEL)
				? AccessSettings.WORKER_SESSION_TIMEOUT : AccessSettings.USER_SESSION_TIMEOUT);
	}
	
	public AuthSession(UserEntity user, String token) {
		this(user);
		this.token = token;
	}

	public AuthSession(UserEntity usr, long timeout2, int acclvl) {
		this.userid = usr.getId();
		this.timeout = timeout2;
		this.accessLevel = acclvl;
	}

	public UserEntity user() {
		return UserDAO.get(userid);
	}
	
	public void defineUser(UserEntity user) {
		this.userid = user.getId();
	}
	
}