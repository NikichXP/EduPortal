package eduportal.dao.entity;

import com.googlecode.objectify.annotation.*;

import lombok.*;

@Entity
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Country extends AbstractEntity {
	
	private static final long serialVersionUID = 4984972422062070828L;
	@Index
	private String name;
	@Index
	private String cyrname;
	protected final long maxIdValue = 99_999;
	
	public Country () {
		super();
	}
	public Country (String title) {
		super();
		this.name = title;
	}
	public Country (String title, String cyrname) {
		super();
		this.name = title;
		this.cyrname = cyrname;
	}
	
	public String getCyrname() {
		return ((cyrname != null) ? cyrname : name);
	}
}
