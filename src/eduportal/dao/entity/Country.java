package eduportal.dao.entity;

import java.util.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class Country {
	
	@Id
	private long id;
	@Index
	private String name;
	
	public Country () {
		do {
			this.id = new Random().nextInt(100_000);
		} while (this.id < 0);
	}
	public Country (String title) {
		do {
			this.id = new Random().nextInt(100_000);
		} while (this.id < 0);
		this.name = title;
	}
	
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String title) {
		this.name = title;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof Country)) {
			return false;
		}
		Country other = (Country) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}
}
