package eduportal.dao.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.*;

@Entity
public class DeletedUser extends UserEntity{
	
	private Date deleted; 
	
	public DeletedUser(UserEntity u) {
		this.setId(u.getId());
		this.setAccessGroup(u.getAccessGroup());
		this.setName(u.getName());
		this.setPass(u.getPass());
		this.setLogin(u.getLogin());
		this.deleted = new Date();
		// TODO More fields!
	}
	
	public Date getDeleted() { return deleted; }

}
