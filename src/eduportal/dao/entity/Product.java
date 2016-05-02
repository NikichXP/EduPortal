package eduportal.dao.entity;

import java.util.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class Product {

	@Id
	private long id;
	@Index
	private String title;
	private String description;
	@Index
	private long cityId;
	@Ignore
	private City city;
	@Index
	private long counrtyid;
	@Index
	private boolean actual; // can be inactive: visible to admin only

	@Deprecated
	public Product() {
		do {
			this.id = new Random().nextInt();
		} while (this.id < 0);
	}

	public Product(String title, String description, City city) {
		do {
			this.id = new Random().nextInt();
		} while (this.id < 0);
		this.title = title;
		this.description = description;
		this.city = city;
		this.cityId = city.getId();
		this.counrtyid = city.getCountryId();
	}

	public long getId() {
		return id;
	}

	public String getStringId() {
		return Long.toHexString(id).substring(0, 4).toUpperCase() + "-"
				+ Long.toHexString(id).substring(4).toUpperCase();
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public long getCityId() {
		return cityId;
	}

	public City getCity() {
		return city;
	}

	public long getCountryId() {
		return counrtyid;
	}

	public void setId(Object id_) {
		if (id_ instanceof String) {
			String id = (String) id_;
			if (id.contains("-")) {
				String[] data = id.split("-");
				this.id = Integer.parseInt(data[0], 16) * 0x10000;
				this.id += Integer.parseInt(data[1], 16);
			} else {
				this.id = Integer.parseInt(id, 16);
			}
		} else if (id_ instanceof Long) {
			this.id = (long) id_;
		}
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public void setCity(City city) {
		this.city = city;
		this.cityId = city.getId();
		this.counrtyid = city.getCountryId();
	}

	public boolean isActual() {
		return actual;
	}

	public void setActual(boolean actual) {
		this.actual = actual;
	}

}
