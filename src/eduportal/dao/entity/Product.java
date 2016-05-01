package eduportal.dao.entity;

import java.util.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class Product {
	
	@Id
	private long id;
	private String title;
	private String description;
	@Index
	private long cityId;
	@Ignore
	private City city;
	@Index
	private long counrtyid;
	
	public Product () {
		do {
			this.id = new Random().nextLong();
		} while (this.id < 0);
	}
	
	public long getId() {
		return id;
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
	public void setId(long id) {
		this.id = id;
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + (int) (cityId ^ (cityId >>> 32));
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Product)) {
			return false;
		}
		Product other = (Product) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (cityId != other.cityId) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}

}
