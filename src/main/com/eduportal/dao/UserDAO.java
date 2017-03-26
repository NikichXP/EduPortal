package com.eduportal.dao;

import com.eduportal.entity.ClientEntity;
import com.eduportal.entity.DeletedUser;
import com.eduportal.entity.Employee;
import com.eduportal.entity.UserEntity;
import com.eduportal.repo.ClientRepository;
import com.eduportal.repo.DeletedUserRepository;
import com.eduportal.repo.UserRepository;
import com.eduportal.AppLoader;
import com.eduportal.repo.EmployeeRepository;
import com.eduportal.util.UserUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UserDAO {

	private static UserRepository userRepo;
	private static ClientRepository clientRepo;
	private static EmployeeRepository empRepo;
	private static String[] credentialVariables = {"mail", "phone"};

	private static ClientRepository clientRepo() {
		if (clientRepo == null) {
			clientRepo = AppLoader.get(ClientRepository.class);
		}
		return clientRepo;
	}

	private static EmployeeRepository empRepo() {
		if (empRepo == null) {
			empRepo = AppLoader.get(EmployeeRepository.class);
		}
		return empRepo;
	}

	private static UserRepository userRepo() {
		if (userRepo == null) {
			userRepo = AppLoader.get(UserRepository.class);
		}
		return userRepo;
	}

	public static List<UserEntity> listAll() {

		return userRepo().findAll();
	}

	public static List<Employee> getCorpEmployees(String corp) {

		return empRepo().findByCorporation(corp);
	}

	public static UserEntity create(String passport, String pass, String name, String surname, String mail,
	                                String phone, Employee creator, Date born) {

		if (userRepo().findByPhoneOrMail(phone, mail) != null) {
			return null;
		}
		if (passport == null) {
			Random r = new Random();
			passport = ((char) (r.nextInt(('Z' - 'A' + 1)) + 'A')) + "" + ((char) (r.nextInt(('Z' - 'A' + 1)) + 'A'))
					+ (r.nextInt(900_000) + 100_000);
		}
		ClientEntity u = new ClientEntity(passport, name, surname, mail, pass, phone, born);
		u.setCreator(creator);
		userRepo().save(u);
		clientRepo().save(u);
		return u;
	}

	public static List<UserEntity> getUnactiveClients(boolean active) {

		return userRepo().findUnactiveUsers(false);
	}

	/**
	 * Performs search through users DB
	 *
	 * @param creds
	 * @return
	 */
	@Deprecated
	public static List<UserEntity> searchUsers(String creds) {

		List<UserEntity> list = userRepo().findAll();
		List<UserEntity> ret = list.stream().filter(x -> x.getMail().matches(creds)).collect(Collectors.toList());
		ret.addAll(list.stream().filter(x -> x.getPhone().matches(creds)).collect(Collectors.toList()));
		return ret;
	}

	public static UserEntity get(String login, String pass) {

		if (pass.length() != UserUtils.CRYPTOLENGTH) {
			pass = UserUtils.encodePass(pass);
		}
		UserEntity u = userRepo().findByPhoneOrMail(login, login);
		if (pass.equals(u.getPass())) {
			if (u.getClassType().equals("client")) {
				return clientRepo().findOne(u.getId());
			} else if (u.getClassType().equals("employee")) {
				return empRepo().findOne(u.getId());
			}
		}
		return null;
	}


	public static void update(UserEntity u) { //TODO Test this
		userRepo().save(u);
		switch (u.getClassType()) {
			case "employee":
				empRepo().save((Employee) u);
				break;
			case "client":
				clientRepo().save((ClientEntity) u);
				break;
		}
	}

	public static UserEntity get(String id) {

		UserEntity u = userRepo().findOne(id);
		if (u.getClassType().equals("employee")) {
			return empRepo().findOne(id);
		} else if (u.getClassType().equals("client")) {
			return clientRepo().findOne(id);
		}
		throw new UnsupportedOperationException("Unknown class");
	}

	public static ClientEntity getClient(String id) {

		return clientRepo().findOne(id);
	}

	public static Employee getEmp(String id) {

		return empRepo().findOne(id);
	}

	public static List<Employee> getAgentsList() {

		return empRepo().findAgents(true);
	}

	public static void delete(String id) {

		UserEntity u = userRepo().findOne(id);
		DeletedUser du = new DeletedUser(u);
		userRepo().delete(u.getId());
		if (u.getClassType().equals("employee")) {
			empRepo().delete(u.getId());
		} else {
			clientRepo().delete(u.getId());
		}
		AppLoader.get(DeletedUserRepository.class).save(du);
	}

	public static UserEntity create(UserEntity user) {

		userRepo().save(user);
		if (user.getClassType().equals("client")) {
			clientRepo().save((ClientEntity) user);
		} else if (user.getClassType().equals("employee")) {
			empRepo().save((Employee) user);
		}
		return get(user.getId());
	}

	public static List<ClientEntity> getClients(UserEntity u) {

		return clientRepo().findCreatedBy(u.getId());
	}

	public static List<UserEntity> searchUsers(String phone, String name, String mail) {

		return userRepo().findAll().stream()
				.filter(x -> phone == null || x.getPhone().matches(phone))
				.filter(x -> name == null || x.getName().matches(name))
				.filter(x -> mail == null || x.getMail().matches(mail))
				.collect(Collectors.toList());
	}

	public static UserEntity getUserByMail(String mail) {

		return userRepo().findByMail(mail);
	}

	public List<Employee> getEmpList() {

		return empRepo().findAll();
	}


}
