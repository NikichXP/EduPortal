package eduportal.model;

import java.io.Serializable;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import eduportal.dao.entity.Employee;
import eduportal.dao.entity.UserEntity;
import lombok.*;

@Entity
public @Data class AuthSession implements Serializable {
	private static final long serialVersionUID = -3213250756514680668L;
	@Id
	private String token;
	private Ref<UserEntity> user;
	@Index
	private long timeout;
	private int accessLevel;
	
	@Deprecated
	public AuthSession() {
	}

	public AuthSession(UserEntity user) {
		this.user = Ref.create(user);
		if (user instanceof Employee == false) {
			this.accessLevel = 0;
		} else {
			this.accessLevel = ((Employee)user).getAccessLevel();
		}
		this.timeout = System.currentTimeMillis() + ((this.accessLevel >= AccessSettings.MODERATOR_LEVEL)
				? AccessSettings.WORKER_SESSION_TIMEOUT : AccessSettings.USER_SESSION_TIMEOUT);
	}
	
	public AuthSession(UserEntity user, String token) {
		this.user = Ref.create(user);
		if (user instanceof Employee == false) {
			this.accessLevel = 0;
		} else {
			this.accessLevel = ((Employee)user).getAccessLevel();
		}
		this.timeout = System.currentTimeMillis() + ((this.accessLevel >= AccessSettings.MODERATOR_LEVEL)
				? AccessSettings.WORKER_SESSION_TIMEOUT : AccessSettings.USER_SESSION_TIMEOUT);
		this.token = token;
	}

	public AuthSession(UserEntity usr, long timeout2, int acclvl) {
		this.user = Ref.create(usr);
		this.timeout = timeout2;
		this.accessLevel = acclvl;
	}

	public UserEntity user() {
		return user.get();
	}

	public void setUser(Ref<UserEntity> user) {
		this.user = user;
	}
	
	public void defineUser(UserEntity user) {
		this.user = Ref.create(user);
	}
	
}