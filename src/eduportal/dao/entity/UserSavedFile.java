package eduportal.dao.entity;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class UserSavedFile {
	
	@Id
	private String id;
	private Key<UserEntity> user;
	
	public UserSavedFile () {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
	}

	public long getUser() {
		return user.getId();
	}

	public void setUser(Key<UserEntity> user) {
		this.user = user;
	}
	
	public UserEntity userEntity () {
		return Ref.create(user).get();
	}

	public void defineUser(UserEntity user2) {
		this.user = Ref.create(user2).getKey();
	}
	
}
