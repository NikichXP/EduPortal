package eduportal.dao.entity;

import java.io.Serializable;
import java.util.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import eduportal.dao.UserDAO;
import eduportal.model.AccessSettings;
import eduportal.util.*;

@Entity
public class UserEntity implements Serializable, Comparable<UserEntity> {
	private static final long serialVersionUID = 609051047144006260L;
	@Id
	private String id;
	@Index
	private String pass;
	@Index
	private String phone;
	@Index
	private String mail;
	@Index
	private String passport;
	private Permission permission;
	@Index
	private long corporation;
	@Index
	private String name;
	@Index
	private String surname;
	@Index
	private Key<UserEntity> creator;
	private int accessLevel;
	@Index
	private boolean isActive;
	private Date born;
	private Date passportActive;
	private ArrayList<SavedFile> files;

	private HashMap<String, String> userData;
	public final static String[] userParams = { "Отчество", "Имя латиницей (как в загран.паспорте)",
			"Фамилия латиницей", "Серия и номер заграничного паспорта", "Почтовый индекс", "Гражданство", "Номер школы",
			"Адрес школы", "Статус обучения (абитуриент, бакалавр...)", "Год окончания обучения", "Место работы",
			"Оконченный ВУЗ (если имеется)", "Адрес ВУЗа (если имеется)", "ФИО отца", "ФИО матери",
			"Фамилия при рождении", "Ранее используемые фамилии", "Гражданство отца", "Гражданство матери",
			"Адрес прописки", "Адрес Skype", "Братья и сестры", "Предпологаемые университеты (минимум 2)",
			"Были ли ранее проблемы, связанные с пребыванием за границей ( "
					+ "нарушения визового режима или закона, депортации, задержания полицией, "
					+ "незаконный перевоз чего — либо и т.п.) (указать какие, если были",
			"Медицинские проблемы (болезни, которые требуют регулярный контроль врача, "
					+ "постоянное употребление каких-либо медикаментов) – для оптимального подбора страховой компании",
			"Противопоказания, аллергии, ограничение по спорту или активному образу жизни",
			"Спорт. Секции, клубы, увлечения, что хотели бы продолжить в стране обучения",
			"Откуда узнали о нашей программе. Кто нас порекомендовал", "Комментарий сотруднику Vedi Tour Group" };

	public boolean hasNull() {
		if (passport == null) {
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

	private void genId() {
		this.id = UUID.randomUUID().toString().substring(0, 8);
	}

	public UserEntity() {
		super();
		genId();
		this.permission = new Permission();
		this.name = "Noname";
		this.surname = "Noname";
		this.files = new ArrayList<>();
		this.passport = "NU";
		for (int i = 0; i < 6; i++) {
			this.passport += (int) (Math.random() * 10);
		}
		this.accessLevel = 0;
		this.isActive = false;
		this.born = new Date(System.currentTimeMillis() - (3600L * 24 * 365 * 20));
		this.passportActive = new Date(System.currentTimeMillis() + (3600L * 24 * 365 * 10));
		this.userData = new HashMap<>();
	}

	public UserEntity(String passport, String name, String surname, String mail, String pass, String phone, Date date) {
		super();
		genId();
		this.passport = passport;
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.phone = phone;
		this.pass = UserUtils.encodePass(pass);
		this.permission = new Permission();
		this.accessLevel = 0;
		this.born = date;
		this.files = new ArrayList<>();
		this.isActive = false;
		this.born = new Date(System.currentTimeMillis() - (3600L * 24 * 365 * 20));
		this.passportActive = new Date(System.currentTimeMillis() + (3600L * 24 * 365 * 10));
		this.userData = new HashMap<>();
	}

	public void addData(String key, String value) {
		this.userData.put(key, value);
	}
	
	public void changeData(String key, String value) {
		this.userData.remove(key);
		this.userData.put(key, value);
	}
	
	public String[][] getSimpleData () {
		Set<String> params = userData.keySet();
		String[][] ret = new String[params.size()][2];
		int i = 0;
		for (String param : params) {
			ret[i][0] = param;
			ret[i][1] = userData.get(param);
			i++;
		}
		return ret;
	}
	
	public String[][] getSimpleDataWithNull() {
		String[][] ret = new String[userParams.length][2];
		int i = 0;
		for (String param : userParams) {
			ret[i][0] = param;
			ret[i][1] = userData.get(param);
			i++;
		}
		return ret;
	}

	public String getData(String key) {
		return this.userData.get(key);
	}

	public void wipeSecData() {
		this.pass = null;
		this.permission = null;
	}

	public UserEntity setAccessLevel(int accessGroup) {
		this.accessLevel = accessGroup;
		if (accessGroup >= AccessSettings.MODERATOR_LEVEL) {
			this.isActive = true;
		}
		return this;
	}

	public UserEntity setCreator(UserEntity creator) {
		this.creator = Ref.create(creator).getKey();
		this.corporation = creator.getCorporation();
		return this;
	}

	public Corporation corporationEntity() {
		return UserDAO.getCorp(corporation);
	}

	public void addFile(SavedFile file) {
		this.files.add(file);
	}

	public void defineCorporation(Corporation corp) {
		this.corporation = corp.getId();
	}

	public void setPass(String pass) {
		if (pass == null) {
			return;
		}
		this.pass = UserUtils.encodePass(pass);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public long getCorporation() {
		return corporation;
	}

	public void setCorporation(long corporation) {
		this.corporation = corporation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public long getCreator() {
		return creator.getId();
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

	public Date getPassportActive() {
		return passportActive;
	}

	public void setPassportActive(Date passportActive) {
		this.passportActive = passportActive;
	}

	public ArrayList<SavedFile> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<SavedFile> files) {
		this.files = files;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPass() {
		return pass;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public HashMap<String, String> getUserData() {
		return userData;
	}

	public void setUserData(HashMap<String, String> userData) {
		this.userData = userData;
	}

	@Override
	public int compareTo(UserEntity o) {
		if (this.getAccessLevel() != o.getAccessLevel()) {
			return o.getAccessLevel() - this.getAccessLevel();
		}
		int ret = this.getName().compareToIgnoreCase(o.getName());
		if (ret == 0) {
			ret = this.getSurname().compareToIgnoreCase(o.getName());
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accessLevel;
		result = prime * result + ((born == null) ? 0 : born.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
		result = prime * result + ((passport == null) ? 0 : passport.hashCode());
		result = prime * result + ((passportActive == null) ? 0 : passportActive.hashCode());
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
		if (accessLevel != other.accessLevel) {
			return false;
		}
		if (born == null) {
			if (other.born != null) {
				return false;
			}
		} else if (!born.equals(other.born)) {
			return false;
		}
		if (creator == null) {
			if (other.creator != null) {
				return false;
			}
		} else if (!creator.equals(other.creator)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
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
		if (passport == null) {
			if (other.passport != null) {
				return false;
			}
		} else if (!passport.equals(other.passport)) {
			return false;
		}
		if (passportActive == null) {
			if (other.passportActive != null) {
				return false;
			}
		} else if (!passportActive.equals(other.passportActive)) {
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
		return "UserEntity [id=" + id + ", pass=" + ((pass.length() == 128) ? "..." : pass) + ", phone=" + phone + ", mail=" + mail + ", passport="
				+ passport + ", permission=" + permission + ", corporation=" + corporation + ", name=" + name
				+ ", surname=" + surname + ", creator=" + creator + ", accessLevel=" + accessLevel + ", isActive="
				+ isActive + ", born=" + born + ", passportActive=" + passportActive + ", files=" + files
				+ ", userData=" + userData + "]";
	}
}