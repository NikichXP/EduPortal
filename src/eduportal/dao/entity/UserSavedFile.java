package eduportal.dao.entity;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.annotation.*;

@Entity
public class UserSavedFile extends AbstractEntity {
	
	@Index
	private String name;
	private Blob file;
	
	public UserSavedFile () {
		super();
	}

	public UserSavedFile(String name2, Blob imageBlob) {
		super();
		this.name = name2;
		this.file = imageBlob;
	}

	public String getName() {
		return name;
	}

	public Blob getFile() {
		return file;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFile(Blob file) {
		this.file = file;
	}
	
	
	
}
