package eduportal.dao.entity;

import java.util.Random;

import com.googlecode.objectify.annotation.*;

@Entity
public class UserEntity {
	
	@Id
	private long id;
    @Index 
    private String login;
    @Index 
    private String pass;
    @Index
    private String phone;
    @Index
    private String mail;
    
    private int accessGroup;
	private String name;
	private String surname;
	
	public boolean hasNull () {
		if (login == null) {
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
	
	public UserEntity() {
		super();
		this.id = new Random().nextLong();
		this.accessGroup = 0;
	}
	public UserEntity (String login, String pass, String name, String surname) {
		super();
		this.id = new Random().nextLong();
		this.name = name;
		this.surname = surname;
		this.login = login;
		this.pass = pass;
		this.accessGroup = 0;
	}
	
	public long getId() {
		return id;
	}
	public String getLogin() {
		return login;
	}
	public String getPass() {
		return pass;
	}
	public int getAccessGroup() {
		return accessGroup;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public void setAccessGroup(int accessGroup) {
		this.accessGroup = accessGroup;
	}
	public UserEntity setAccessGroupR(int accessGroup) {
		this.accessGroup = accessGroup;
		return this;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}
	public String getMail() {
		return mail;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessGroup;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
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
		if (!(obj instanceof UserEntity)) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
		if (accessGroup != other.accessGroup) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		if (mail == null) {
			if (other.mail != null) {
				return false;
			}
		} else if (!mail.equals(other.mail)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (pass == null) {
			if (other.pass != null) {
				return false;
			}
		} else if (!pass.equals(other.pass)) {
			return false;
		}
		if (phone == null) {
			if (other.phone != null) {
				return false;
			}
		} else if (!phone.equals(other.phone)) {
			return false;
		}
		if (surname == null) {
			if (other.surname != null) {
				return false;
			}
		} else if (!surname.equals(other.surname)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", login=" + login + ", pass=" + pass + ", phone=" + phone + ", mail=" + mail
				+ ", accessGroup=" + accessGroup + ", name=" + name + ", surname=" + surname + "]";
	}
	
}
