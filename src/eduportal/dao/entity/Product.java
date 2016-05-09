package eduportal.dao.entity;

import java.util.Random;
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
	private boolean actual;
	@Index
	private double defaultPrice;
	
	public Product () {
		
	}
	
	public Product (String name, String descr, City c) {
		do {
			this.id = new Random().nextInt(100_000);
		} while (this.id < 0);
		this.title = name;
		this.description = descr;
		this.city = c;
		this.cityId = c.getId();
		this.counrtyid = c.getCountryId();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public long getCounrtyid() {
		return counrtyid;
	}
	public void setCounrtyid(long counrtyid) {
		this.counrtyid = counrtyid;
	}
	public boolean isActual() {
		return actual;
	}
	public void setActual(boolean actual) {
		this.actual = actual;
	}
	
	public double getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(double defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (actual ? 1231 : 1237);
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + (int) (cityId ^ (cityId >>> 32));
		result = prime * result + (int) (counrtyid ^ (counrtyid >>> 32));
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (actual != other.actual)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (cityId != other.cityId)
			return false;
		if (counrtyid != other.counrtyid)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
