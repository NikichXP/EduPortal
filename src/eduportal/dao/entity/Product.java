package eduportal.dao.entity;

import java.util.ArrayList;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class Product extends AbstractEntity {

	private static final long serialVersionUID = 8913130263468249461L;
	@Index
	private String title;
	private String description;
	@Index
	private Key<City> city;
	@Index
	private boolean actual;
	@Index
	private double defaultPrice;
	private String start;
	private String end;
	private ArrayList<SavedFile> files;
	private String currency;
	private String provider;
	
	public Product () {
		files = new ArrayList<>();
		this.start = "01/09";
		this.end = "31/05";
	}
	
	public Product (String name, String descr, City c) {
		super();
		this.title = name;
		this.description = descr;
		this.city = Ref.create(c).getKey();
		files = new ArrayList<>();
		this.start = "01/09";
		this.end = "31/05";
	}
	
	public City getCity() {
		return Ref.create(city).get();
	}
	public void setCity(City city) {
		this.city = Ref.create(city).getKey();
	}

	public Product setDefaultPrice(double defaultPrice) {
		this.defaultPrice = defaultPrice;
		return this;
	}
	
	public void addFile (SavedFile file) {
		this.files.add(file);
	}

}
