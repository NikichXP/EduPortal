package eduportal.dao.entity;

import com.googlecode.objectify.annotation.*;

@Entity
public class SavedFile {
	
	@Id
	private String id;
	
	public SavedFile () {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String name) {
		this.id = name;
	}
	
}
