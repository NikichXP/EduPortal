package com.eduportal.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.HashSet;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Employee extends UserEntity {
	
	private static final long serialVersionUID = 32648756287346L;

	private HashSet<Long> country;
	private int accessLevel;
	private String corporation;
	private boolean isAgent;
	
	public Employee() {
		super();
		isAgent = false;
		country = new HashSet<>();
	}
	
	public Employee(String string, String string2, String string3, String string4, String string5, String string6,
			Date date) {
		super(string, string2, string3, string4, string5, string6, date);
		isAgent = false;
		country = new HashSet<>();
	}

	@Override
	public String getClassType() {
		return "employee";
	}


	public Employee setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
		return this;
	}
	
//	public Country[] getCountryList () {
//		return country.toArray(new Country[country.size()]);
//	}

}
