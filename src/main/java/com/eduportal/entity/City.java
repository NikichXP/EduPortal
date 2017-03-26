package com.eduportal.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
@EqualsAndHashCode
public class City {
	private static final long serialVersionUID = -2792000127515334576L;
	@Id
	private String id;
	private String country;
	private String name, cyrname;
	protected final long maxIdValue = 999_999;

	public String getCyrname() {
		return ((cyrname != null) ? cyrname : name);
	}
}
