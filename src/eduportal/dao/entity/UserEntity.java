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
    
    private int accessGroup;
	private String name;
	private String surname;
	@Ignore
	private String token;
	
	public UserEntity() {
		super();
		this.id = new Random().nextLong();
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
	public String getToken() {
		return token;
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
	public void setName(String name) {
		this.name = name;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessGroup;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
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
		if (surname == null) {
			if (other.surname != null) {
				return false;
			}
		} else if (!surname.equals(other.surname)) {
			return false;
		}
		return true;
	}
    
    
    
}
