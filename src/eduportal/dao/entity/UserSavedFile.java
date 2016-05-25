package eduportal.dao.entity;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class UserSavedFile {
	
	@Id
	private String id;
	private Ref<UserEntity> user;
	
	public UserSavedFile () {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
	}

	public Ref<UserEntity> getUser() {
		return user;
	}

	public void setUser(Ref<UserEntity> user) {
		this.user = user;
	}
	
	public UserEntity userEntity () {
		return user.get();
	}

	public void defineUser(UserEntity user2) {
		this.user = Ref.create(user2);
	}
	
}
