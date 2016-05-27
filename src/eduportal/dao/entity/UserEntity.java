package eduportal.dao.entity;

import java.io.Serializable;
import java.util.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import eduportal.dao.UserDAO;
import eduportal.util.*;

@Entity
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 609051047144006260L;
	@Id
	private String id; // THIS IS PASSPORT ID
	@Index
	private String pass;
	@Index
	private String phone;
	@Index
	private String mail;
	private Permission permission;
	@Index
	private long corporation;
	private int accessLevel;
	private String cyrillicName;
	private String cyrillicSurname;
	@Index
	private String name;
	@Index
	private String surname;
	@Index
	private Key<UserEntity> creator;
	private Date born;
	private long orderId;
	private ArrayList<SavedFile> files;

	public boolean hasNull() {
		if (id == null) {
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
		this.permission = new Permission();
		this.name = "Noname";
		this.surname = "Noname";
		this.cyrillicName = "Неопознанный";
		this.cyrillicSurname = ((Math.random() > 0.5) ? "Баклажан" : "Кекс");
		this.files = new ArrayList<>();
		this.id = "NU";
		this.accessLevel = 0;
		for (int i = 0; i < 6; i++) {
			this.id += (int) (Math.random()*10);
		}
	}

	public UserEntity(String id, String name, String surname, String cyrillicName, String cyrillicSurname, String mail,
			String pass, String phone, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.phone = phone;
		this.pass = UserUtils.encodePass(pass);
		this.permission = new Permission();
		this.cyrillicName = cyrillicName;
		this.cyrillicSurname = cyrillicSurname;
		this.accessLevel = 0;
		this.born = date;
		this.files = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCyrillicData() {
		return cyrillicName + " " + cyrillicSurname;
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

	public String getCyrillicName() {
		return cyrillicName;
	}

	public String getCyrillicSurname() {
		return cyrillicSurname;
	}

	public void setCyrillicName(String cyrillicName) {
		this.cyrillicName = cyrillicName;
	}

	public void setCyrillicSurname(String cyrillicSurname) {
		this.cyrillicSurname = cyrillicSurname;
	}

	public long getCorporation() {
		return corporation;
	}

	public void setCorporation(long corporation) {
		this.corporation = corporation;
	}
	
	public void defineCorporation (Corporation corp) {
		this.corporation = corp.getId();
	}

	public String getPass() {
		return pass;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public void setPass(String pass) {
		if (pass == null) {
			return;
		}
		this.pass = UserUtils.encodePass(pass);
	}

	public UserEntity setAccessLevel(int accessGroup) {
		this.accessLevel = accessGroup;
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

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getCreator() {
		return creator.getId();
	}

	public UserEntity creatorEntity() {
		return Ref.create(creator).get();
	}

	public UserEntity setCreator(UserEntity creator) {
		this.creator = Ref.create(creator).getKey();
		this.corporation = creator.getCorporation();
		return this;
	}

	public Corporation corporationEntity() {
		return UserDAO.getCorp(corporation);
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void addFile (SavedFile file) {
		this.files.add(file);
	}

	public ArrayList<SavedFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<SavedFile> files) {
		this.files = files;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
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
		if (permission == null) {
			if (other.permission != null) {
				return false;
			}
		} else if (!permission.equals(other.permission)) {
			return false;
		}
		if (creator == null) {
			if (other.creator != null) {
				return false;
			}
		} else if (!creator.equals(other.creator)) {
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
		return "UserEntity [id=" + id + ", pass=" + pass + ", phone=" + phone + ", mail=" + mail + ", permission="
				+ permission + ", corporation=" + corporation + ", accessLevel=" + accessLevel + ", cyrillicName="
				+ cyrillicName + ", cyrillicSurname=" + cyrillicSurname + ", name=" + name + ", surname=" + surname
				+ ", creator=" + creator + ", born=" + born + ", orderId=" + orderId + ", files=" + files + "]";
	}

	public void wipeSecData() {
		this.pass = null;
		this.permission = null;
	}
	
}
