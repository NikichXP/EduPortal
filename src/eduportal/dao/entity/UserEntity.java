package eduportal.dao.entity;

import java.io.Serializable;
import java.util.*;
import com.googlecode.objectify.*;
import com.googlecode.objectify.annotation.*;

import eduportal.dao.UserDAO;
import eduportal.model.AccessSettings;
import eduportal.util.*;

@Entity
public class UserEntity implements Serializable {
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
	private int accessLevel;
	private String cyrillicName;
	private String cyrillicSurname;
	private String cyrillicFathername;
	@Index
	private String name;
	@Index
	private String surname;
	@Index
	private Key<UserEntity> creator;
	private Date born;
	private Date passportActive;
	private ArrayList<SavedFile> files;
	@Index
	private boolean isActive;
	private int postindex;
	private Key<Country> citizen;
	private String school;
	private String homeAddr;
	private String comment; // Комментарий на момент создания пользователя
	private String orderdata;
	private int year;
	private long orderId;

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
		this.cyrillicName = "Неопознанный";
		this.cyrillicSurname = ((Math.random() > 0.5) ? "Баклажан" : "Кекс");
		this.cyrillicFathername = "Войдович";
		this.files = new ArrayList<>();
		this.passport = "NU";
		for (int i = 0; i < 6; i++) {
			this.passport += (int) (Math.random() * 10);
		}
		this.accessLevel = 0;
		this.isActive = false;
		this.born = new Date(System.currentTimeMillis() - (3600L * 24 * 365 * 20));
		this.passportActive = new Date(System.currentTimeMillis() + (3600L * 24 * 365 * 10));
		this.citizen = Ref.create(AccessSettings.DEFAULT_COUNTRY).getKey();
	}

	public UserEntity(String passport, String name, String surname, String cyrillicName, String cyrillicSurname, String mail,
			String pass, String phone, Date date) {
		super();
		genId();
		this.passport = passport;
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
		this.isActive = false;
		this.born = new Date(System.currentTimeMillis() - (3600L * 24 * 365 * 20));
		this.passportActive = new Date(System.currentTimeMillis() + (3600L * 24 * 365 * 10));
		this.citizen = Ref.create(AccessSettings.DEFAULT_COUNTRY).getKey();
	}

	public void wipeSecData() {
		this.pass = null;
		this.permission = null;
	}

	public String getCyrillicData() {
		return cyrillicName + " " + cyrillicSurname + " " + cyrillicFathername;
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

	public String getCyrillicName() {
		return cyrillicName;
	}

	public void setCyrillicName(String cyrillicName) {
		this.cyrillicName = cyrillicName;
	}

	public String getCyrillicSurname() {
		return cyrillicSurname;
	}

	public void setCyrillicSurname(String cyrillicSurname) {
		this.cyrillicSurname = cyrillicSurname;
	}

	public String getCyrillicFathername() {
		return cyrillicFathername;
	}

	public void setCyrillicFathername(String cyrillicFathername) {
		this.cyrillicFathername = cyrillicFathername;
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

	public int getPostindex() {
		return postindex;
	}

	public void setPostindex(int postindex) {
		this.postindex = postindex;
	}

	public long getCitizen() {
		return citizen.getId();
	}

	public void setCitizen(Country citizen) {
		this.citizen = Ref.create(citizen).getKey();
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getHomeAddr() {
		return homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOrderdata() {
		return orderdata;
	}

	public void setOrderdata(String orderdata) {
		this.orderdata = orderdata;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPass() {
		return pass;
	}

	public int getAccessLevel() {
		return accessLevel;
	}
	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", pass=" + ((pass.length() == 128) ? "..." : pass) + ", phone=" + phone + ", mail=" + mail + ", passport="
				+ passport + ", permission=" + permission + ", corporation=" + corporation + ", accessLevel="
				+ accessLevel + ", cyrillicName=" + cyrillicName + ", cyrillicSurname=" + cyrillicSurname
				+ ", cyrillicFathername=" + cyrillicFathername + ", name=" + name + ", surname=" + surname
				+ ", creator=" + creator + ", born=" + born + ", passportActive=" + passportActive + ", files=" + files
				+ ", isActive=" + isActive + ", postindex=" + postindex + ", citizen=" + citizen + ", school=" + school
				+ ", homeAddr=" + homeAddr + ", comment=" + comment + ", orderdata=" + orderdata + ", year=" + year
				+ ", orderId=" + orderId + "]";
	}
	
}