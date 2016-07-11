package eduportal.dao.entity;

import java.util.*;
import com.googlecode.objectify.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Subclass(index=true)
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Employee extends UserEntity {
	
	private static final long serialVersionUID = 32648756287346L;
	/** @see AccessSettings.class */
	private HashSet<Long> country;
	private int accessLevel;
	@Index
	private String corporation;
	
	public Employee() {
		super();
		country = new HashSet<>();
	}
	
	public Employee(String string, String string2, String string3, String string4, String string5, String string6,
			Date date) {
		super(string, string2, string3, string4, string5, string6, date);
		country = new HashSet<>();
	}
	public Employee setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
		return this;
	}
	
	public Country[] getCountryList () {
		return country.toArray(new Country[country.size()]);
	}

}
