package eduportal.dao.entity;

import java.util.Random;

import com.googlecode.objectify.annotation.*;

@Entity
public class City {
	
	@Id
	private long id;
	@Ignore
	private Country country;
	private long countryId;
	@Index
	private String name;
	
	public City () {
		do {
			this.id = new Random().nextLong();
		} while (this.id < 0);
	}
	
	public long getId() {
		return id;
	}
	public Country getCountry() {
		return country;
	}
	public long getCountryId() {
		return countryId;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String city) {
		this.name = city;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + (int) (countryId ^ (countryId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof City)) {
			return false;
		}
		City other = (City) obj;
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) {
			return false;
		}
		if (countryId != other.countryId) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", country=" + country + ", countryId=" + countryId + "]";
	}
}
