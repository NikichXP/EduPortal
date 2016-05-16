package eduportal.dao.entity;

import java.util.ArrayList;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import eduportal.util.UserUtils;

@Entity
public class UserEntity extends AbstractEntity {

	@Index
	private String login;	
	/**
	 * Pass is always encoded
	 */
	@Index
	private String pass;
	@Index
	private String phone;
	@Index
	private String mail;
	private Permission accessLevel;
	@Index
	private String name;
	@Index
	private String surname;
	@Index
	private Key<UserEntity> creator;
	private ArrayList<Long> ordersId;
		
	public void addOrder (Order ord) {
		long id = ord.getId();
		for (long l : ordersId) {
			if (l == id) {
				return;
			}
		}
		ordersId.add(ord.getId());
	}

	public boolean hasNull() {
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
		this.ordersId = new ArrayList<>();
		this.accessLevel = new Permission();
	}

	public UserEntity(String login, String pass, String name, String surname, String phone, String mail) {
		super();
		this.name = name;
		this.surname = surname;
		this.login = login;
		this.mail = mail;
		this.phone = phone;
		this.pass = UserUtils.encodePass(pass);
		this.accessLevel = new Permission();
		this.ordersId = new ArrayList<>();
	}

	public String getLogin() {
		return login;
	}

	public String getPass() {
		return pass;
	}

	public int accessGroup() {
		return accessLevel.getAccessGroup();
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPass(String pass) {
		if (pass == null) {
			return;
		}
		this.pass = UserUtils.encodePass(pass);
	}

	public UserEntity defineAccessGroup(int accessGroup) {
		this.accessLevel.setAccessGroup(accessGroup);
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
	
	public ArrayList<Long> getOrdersId() {
		return this.ordersId;
	}

	public void setOrders(Order[] orders) {
		this.ordersId.ensureCapacity(orders.length);
		for (Order ord : orders) {
			this.ordersId.add(ord.getId());
		}
	}

	public long getCreator() {
		return creator.getId();
	}
	
	public UserEntity creatorEntity() {
		return Ref.create(creator).get();
	}

	public UserEntity setCreator(UserEntity creator) {
		this.creator = Ref.create(creator).getKey();
		this.accessLevel.setCorporation(creator.getAccessLevel().corporationEntity());
		return this;
	}

	public Permission getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Permission accessLevel) {
		this.accessLevel = accessLevel;
	}

	public void setOrdersId(ArrayList<Long> ordersId) {
		this.ordersId = ordersId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessLevel == null) ? 0 : accessLevel.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ordersId == null) ? 0 : ordersId.hashCode());
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
		if (accessLevel == null) {
			if (other.accessLevel != null) {
				return false;
			}
		} else if (!accessLevel.equals(other.accessLevel)) {
			return false;
		}
		if (creator == null) {
			if (other.creator != null) {
				return false;
			}
		} else if (!creator.equals(other.creator)) {
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
		if (ordersId == null) {
			if (other.ordersId != null) {
				return false;
			}
		} else if (!ordersId.equals(other.ordersId)) {
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
		return "UserEntity [login=" + login + ", pass=" + pass + ", phone=" + phone + ", mail=" + mail
				+ ", accessLevel=" + accessLevel + ", name=" + name + ", surname=" + surname + ", creator=" + creator
				+ ", ordersId=" + ordersId + "]";
	}

	public void wipeSecData() {
		this.pass = null;
	}
	
}
