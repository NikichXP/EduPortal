package eduportal.dao.entity;

import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class City extends AbstractEntity {
	private static final long serialVersionUID = -2792000127515334576L;
	private Key<Country> country;
	@Index
	private String name;
	@Index
	private String cyrname;
	protected final long maxIdValue = 999_999;

	public Country getCountry() {
		return Ref.create(country).get();
	}

	public void setCountry(Country country) {
		this.country = Ref.create(country).getKey();
	}

	public String getCyrname() {
		return ((cyrname != null) ? cyrname : name);
	}
}
