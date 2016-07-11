package eduportal.dao.entity;

import java.io.Serializable;
import java.util.Random;

import com.googlecode.objectify.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public abstract class AbstractEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3463460744465924474L;
	
	@Id
	protected long id;
	/**
	 * Max ID value, use 0 to unlimit it
	 */
	protected final long maxIdValue = Integer.MAX_VALUE;

	@SuppressWarnings("unused")
	public AbstractEntity () {
		if (maxIdValue != 0) {
			do {
				id = new Random().nextInt((int)maxIdValue);
			} while (id <= 0);
		} else {
			do {
				id = new Random().nextLong();
			} while (id <= 0);
		}
	}
	
	public String getIdString () {
		return Long.toString(id);
	}
	
	public String getIdHexString () {
		return Long.toHexString(id);
	}
}
