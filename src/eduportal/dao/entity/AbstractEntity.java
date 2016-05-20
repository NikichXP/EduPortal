package eduportal.dao.entity;

import java.util.Random;

import com.googlecode.objectify.annotation.*;

@Entity
public abstract class AbstractEntity {
	
	@Id
	protected long id;
	/**
	 * Max ID value, use 0 to unlimit it
	 */
	protected final int maxIdValue = Integer.MAX_VALUE;

	@SuppressWarnings("unused")
	public AbstractEntity () {
		if (maxIdValue != 0) {
			do {
				id = new Random().nextInt(maxIdValue);
			} while (id <= 0);
		} else {
			do {
				id = new Random().nextLong();
			} while (id <= 0);
		}
	}
	
	public long getId() {
		return id;
	}
	
	public String getIdString () {
		return Long.toString(id);
	}
	
	public String getIdHexString () {
		return Long.toHexString(id);
	}

	public void setId (long id) {
		this.id = id;
	}
}
