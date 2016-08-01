package eduportal.dao.entity;

import java.io.Serializable;
import java.util.*;
import com.googlecode.objectify.annotation.*;
import eduportal.dao.UserDAO;
import eduportal.util.*;
import lombok.*;

@Entity
@ToString
@Data
public class UserEntity implements Serializable, Comparable<UserEntity> {
	private static final long serialVersionUID = 609051047144006260L;
	@Id
	protected String id;
	@Index
	protected String pass;
	@Index
	protected String phone;
	@Index
	protected String mail;
	@Index
	protected String name;
	@Index
	protected String surname;
	@Index
	protected String fathersname;
	protected String birthDate;
	protected ArrayList<SavedFile> files;
	@Serialize
	protected HashMap<String, String> userData;
	@Index
	protected String creator;
	@Index
	protected boolean isActive;
	protected boolean isFinal;

	public boolean hasNull() {
		if (fathersname == null) {
			return true;
		} else if (pass == null) {
			return true;
		} else if (phone == null) {
			return true;
		} else if (mail == null) {
			return true;
		} else if (name == null) {
			return true;
		} else if (surname == null) {
			return true;
		} else {
			return false;
		}
	}

	protected void genId() {
		this.id = UUID.randomUUID().toString().substring(0, 8);
	}

	public UserEntity() {
		super();
		genId();
		this.name = "Noname";
		this.surname = "Noname";
		this.files = new ArrayList<>();
		this.userData = new HashMap<>();
		this.isFinal = false;
	}

	public UserEntity(String passport, String name, String surname, String mail, String pass, String phone, Date date) {
		super();
		genId();
		this.fathersname = passport;
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.phone = phone;
		this.pass = UserUtils.encodePass(pass);
		this.files = new ArrayList<>();
		this.userData = new HashMap<>();
		this.isFinal = false;
	}

	public void addData(String key, String value) {
		this.userData.put(key, value);
	}

	public void putData(String key, String value) {
		this.userData.put(key, value);
	}

	public HashMap<String, String> toMap() {
		return userData;
	}

	public String[][] getSimpleData() {
		Set<String> params = this.toMap().keySet();
		String[][] ret = new String[params.size()][2];
		int i = 0;
		for (String param : params) {
			ret[i][0] = param;
			ret[i][1] = this.toMap().get(param);
			i++;
		}
		return ret;
	}

	public String getData(String key) {
		return this.userData.get(key);
	}

	public UserEntity wipeSecData() {
		this.pass = null;
		return this;
	}

	public void addFile(SavedFile file) {
		this.files.add(file);
	}

	public void setPass(String pass) {
		if (pass == null) {
			return;
		}
		this.pass = UserUtils.encodePass(pass);
	}

	public Employee creatorEntity() {
		return (Employee) UserDAO.get(creator);
	}

	@Override
	public int compareTo(UserEntity o) {
		// if (this.getAccessLevel() != o.getAccessLevel()) {
		// return o.getAccessLevel() - this.getAccessLevel();
		// }
		int ret = this.name.compareToIgnoreCase(o.getName());
		if (ret == 0) {
			ret = this.surname.compareToIgnoreCase(o.getName());
		}
		return ret;
	}

}