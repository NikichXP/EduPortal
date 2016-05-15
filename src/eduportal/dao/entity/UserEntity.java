package eduportal.dao.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

@Entity
public class UserEntity extends AbstractEntity {

	@Index
	private String login;
	@Index
	/**
	 * Pass is always encoded
	 */
	private String pass;
	@Index
	private String phone;
	@Index
	private String mail;
	@Index
	private int accessGroup = 0;
	@Index
	private String name;
	@Index
	private String surname;
	@Index
	private Key<UserEntity> creator;
	private ArrayList<Long> ordersId;
	
	private static MessageDigest mDigest = null;
	static {
		try {
			mDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
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
	}

	public UserEntity(String login, String pass, String name, String surname, String phone, String mail) {
		super();
		this.name = name;
		this.surname = surname;
		this.login = login;
		this.mail = mail;
		this.phone = phone;
		this.pass = encodePass(pass);
		this.accessGroup = 0;
		this.ordersId = new ArrayList<>();
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

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPass(String pass) {
		if (pass == null) {
			return;
		}
		this.pass = encodePass(pass);
	}

	public UserEntity setAccessGroup(int accessGroup) {
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
		return this;
	}

	/** Encodes password with SHA-512 */
	public static String encodePass (String pass) {
		byte[] result = mDigest.digest(pass.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
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
		return "UserEntity [login=" + login + ", pass=" + pass + ", phone=" + phone + ", mail=" + mail
				+ ", accessGroup=" + accessGroup + ", name=" + name + ", surname=" + surname + ", ordersId=" + ordersId
				+ "]";
	}

}
