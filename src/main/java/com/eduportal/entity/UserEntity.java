package com.eduportal.entity;

import com.eduportal.dao.UserDAO;
import com.eduportal.util.UserUtils;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.*;

@ToString
@Data
public abstract class UserEntity implements Serializable, Comparable<UserEntity> {
	private static final long serialVersionUID = 609051047144006260L;
	@Id
	protected String id;
	protected String pass, phone, mail, name, surname, fathersname, born;
	protected ArrayList<SavedFile> files;
	protected HashMap<String, String> userData;
	protected String creator;
	protected boolean isActive;
	protected String entityClassName;

	public UserEntity() {
		super();
		genId();
		this.name = "Noname";
		this.surname = "Noname";
		this.files = new ArrayList<>();
		this.userData = new HashMap<>();
	}

	public UserEntity(String fathersName, String name, String surname, String mail, String pass, String phone,
	                  Date date) {
		super();
		genId();
		this.fathersname = fathersName;
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.phone = phone;
		this.pass = UserUtils.encodePass( pass );
		this.files = new ArrayList<>();
		this.userData = new HashMap<>();
	}

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

	public abstract String getClassType();

	protected void genId() {
		this.id = UUID.randomUUID().toString().substring(0, 8);
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

	public boolean getRealActive() {
		return isActive;
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
		if (pass.length() == 128) {
			this.pass = pass;
		} else {
			this.pass = UserUtils.encodePass( pass );
		}
	}

	public void setCreator(Employee creator) {
		this.creator = creator.getId();
	}

	public void setCreator(String id) {
		this.creator = id;
	}

	public Employee creatorEntity () {
		return (Employee) UserDAO.get( creator );
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