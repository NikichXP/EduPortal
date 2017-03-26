package com.eduportal.entity;

import java.util.Date;

public class DeletedUser extends UserEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5368618189899022227L;
	private Date deleted; 
	
	public DeletedUser(UserEntity u) {
		//TODO Deleted User
	}
	
	public Date getDeleted() { return deleted; }

	@Override
	public String getClassType () {
		return null;
	}
}
