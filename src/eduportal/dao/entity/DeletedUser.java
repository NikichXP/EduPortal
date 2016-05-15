package eduportal.dao.entity;

import java.util.Date;

import com.googlecode.objectify.annotation.*;

@Entity
public class DeletedUser extends UserEntity{
	
	private Date deleted; 
	
	public DeletedUser(UserEntity u) {
		//TODO Deleted User
	}
	
	public Date getDeleted() { return deleted; }

}
