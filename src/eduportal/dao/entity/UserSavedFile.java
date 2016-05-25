package eduportal.dao.entity;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class UserSavedFile {
	
	@Id
	private String id;
	private Blob file;
	private Ref<UserEntity> user;
	
	public UserSavedFile () {
		super();
	}

	public UserSavedFile(String name2, Blob imageBlob) {
		super();
		this.id = name2;
		this.file = imageBlob;
	}

	public String getId() {
		return id;
	}

	public Blob getFile() {
		return file;
	}

	public void setId(String name) {
		this.id = name;
	}

	public void setFile(Blob file) {
		this.file = file;
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
	
}
