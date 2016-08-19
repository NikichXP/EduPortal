package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import javax.servlet.http.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;

import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "test", version = "v1")
public class TestAPI {

	@ApiMethod(path = "test", httpMethod = "GET")
	public List<Object> test() {
		ArrayList<Object> ret = new ArrayList<>();
		List<Product> prodlist = OrderDAO.getProductsByCompany(AccessSettings.OWNERCORP_NAME);
		ret.add(prodlist);
		for (Product prod: prodlist) {
			ret.add(OrderDAO.getOrdersByProduct(prod));
		}
		ret.add(ofy().load().type(Order.class).list());
		return ret;
	}
	
	@ApiMethod(path = "test1", httpMethod = "GET")
	public List<Object> test1() {
		ArrayList<Object> ret = new ArrayList<>();
		UserEntity u = UserDAO.get("admin@corp.com", "pass");
		ret.add(u);
		ret.add(u.toString());
		return ret;
	}

	@ApiMethod(path = "cookies", httpMethod = "GET")
	public ArrayList<Object> testCookies(HttpServletRequest req) {
		ArrayList<Object> ret = new ArrayList<>();
		for (Cookie c : req.getCookies()) {
			ret.add(c.getName() + " " + c.getValue());
		}
		return ret;
	}

	@ApiMethod(name = "listSessions", path = "listsession", httpMethod = "GET")
	public List<String> listSession() {
		return AuthContainer.testMethod();
	}

	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping() {
		return new Text("ping");
	}

	@ApiMethod(name = "Rebuild__DB", path = "rebuildDB", httpMethod = "GET")
	public List<String> rebuildDB(@Named("size") String size, @Named("usersize") @Nullable Integer usersize,
			@Named("ordersize") @Nullable Integer ordersize, @Named("shuffled") @Nullable Boolean shuffled,
			@Named("prior") @Nullable Integer prior) {
		ofy().cache(true).flush();
		for (Class<?> clazz : UserAPI.objectifiedClasses) {
			for (Object u : ofy().load().kind(clazz.getSimpleName()).list()) {
				ofy().delete().entity(u).now();
			}
		}
		Employee[] admins = {
				new Employee("AA555555", "Admin", "Adminov", "admin@corp.com", "pass", "+123456789012", new Date())
						.setAccessLevel(AccessSettings.ADMIN_LEVEL + 1),
				new Employee("SQ512312", "New", "Order", "order@corp.com", "order", "+123456789015", new Date())
						.setAccessLevel(AccessSettings.MODERATOR_LEVEL),
				new Employee("RZ412231", "Adminus", "Maximus", "adminus@corp.com", "adminus", "+123456789016",
						new Date()).setAccessLevel(AccessSettings.MODERATOR_LEVEL),
				new Employee("VW155555", "User", "User", "user@corp.com", "user", "+123456789013", new Date())
						.setAccessLevel(AccessSettings.MODERATOR_LEVEL),
				new Employee("JD151995", "John", "Doe", "john@doe.com", "johndoe", "+123456789014", new Date())
						.setAccessLevel(AccessSettings.MODERATOR_LEVEL) };
		for (Employee user : admins) {
			user.setCreator(admins[0].getId());
			user.setCorporation(AccessSettings.OWNERCORP_NAME);
			user.setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12))
					+ "-" + (1 + (int) (Math.random() * 31)));
			user.addData("This is works", "YEAH");
		}
		ClientEntity[] clients;
		int dbsize;
		if (size == null) {
			size = "norm";
		}
		switch (size) {
		case "big":
			dbsize = 100;
			break;
		case "large":
			dbsize = 500;
			break;
		case "ultra":
			dbsize = 1_000;
			break;
		case "mega":
			dbsize = 10_000;
			break;
		case "fuck":
			dbsize = 512_000;
			break;
		default:
			dbsize = 20;
			break;
		}
		if (usersize == null) {
			usersize = 100;
		}
		clients = new ClientEntity[usersize];
		String[] clientName = NameGen.genNames(clients.length * 2);
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new ClientEntity();
			clients[i].setName(clientName[2 * i]);
			clients[i].setSurname(clientName[2 * i + 1]);
			clients[i].setPass("pass" + i);
			clients[i].setMail("user" + i + "@corp.com");
			clients[i].setPhone("+5555" + ((i < 10) ? "00" + i : (i > 9 && i < 100) ? "0" + i : i) + "12345");
			clients[i].setCreator(admins[i % admins.length].getId());
			clients[i].setCurator(admins[i % admins.length]);
			clients[i].setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12))
					+ "-" + (1 + (int) (Math.random() * 31)));
			clients[i].addData("Адрес Skype", "skuser" + i);
			clients[i].addData("Год окончания обучения", (2010 + (int) (Math.random() * 7)) + "");
			if (Math.random() < 0.7) {
				for (String str : ClientEntity.userParams) {
					StringBuilder sb = new StringBuilder();
					for (String nnn: NameGen.genNames((int)(Math.random()*10) + 1)) {
						sb.append(nnn);
					}
					clients[i].addData(str, sb.toString());
				}
				clients[i].setActive(true);
			}
		}
		if (prior != null) {
			if (prior < admins.length) {
				for (UserEntity u : clients) {
					if (Math.random() > 0.33) {
						u.setCreator(admins[prior].getId());
					}
				}
			}
		}
		ClientEntity test1 = new ClientEntity("Testovich", "Test", "Testov", "test@qwe.rty", "test", "+123455556789", new Date());
		test1.setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12))
					+ "-" + (1 + (int) (Math.random() * 31)));
		test1.setActive(true);
		for (String str : ClientEntity.userParams) {
			StringBuilder sb = new StringBuilder();
			for (String nnn: NameGen.genNames((int)(Math.random()*10) + 1)) {
				sb.append(nnn);
			}
			test1.addData(str, sb.toString());
		}
		test1.setCreator(admins[0].getId());
		test1.setCurator(admins[0]);
		test1.setActive(true);
		ofy().save().entity(test1);
		ofy().save().entities(admins);
		ofy().save().entities(clients);
		City[] c = { GeoDAO.createCity("Kiev", "Ukraine"), GeoDAO.createCity("Lvov", "Ukraine"),
				GeoDAO.createCity("Prague", "Czech Republic"), GeoDAO.createCity("Budapest", "Hungary"),
				GeoDAO.createCity("London", "United Kingdom") };
		Product p[] = { new Product("Test product", "Some product to test", c[0]),
				new Product("NewOrderTest", "Some product to test new order", c[1]),
				new Product("Prague Study School", "Nice Prague school of english, .......", c[2]),
				new Product("Высшая школа Будапешта", "Описание программы", c[3]),
				new Product("Высшая школа Лондона", "Описание программы", c[4]),
				new Product("LSE", "Описание программы", c[4]), 
				new Product("Ещё один ВУЗ", "Описание программы", c[2]),
				new Product("КПИ", "Как же без него?", c[0]), };
		for (Product prod : p) {
			prod.setActual(Math.random() < 0.75); // chance vary!
			prod.setDefaultPrice((double) Math.round(Math.random() * 100_000_00) / 100);
			prod.setProvider((Math.random() < 0.66) ? AccessSettings.OWNERCORP_NAME : "GlobalEdu");
			prod.setCurrency("UAH");
		}
		ProductDAO.save(p);
		Random r = new Random();
		int i = 0;
		for (ClientEntity cli : clients) {
			if (cli.isActive()) {
				Order ord = new Order();
				cli.setOrderid(ord.getId());
				ord.setClient(cli);
				ord.setProduct(p[r.nextInt(p.length)]);
				ord.setCreatedBy(admins[i++ % admins.length]);
				if (Math.random() > 0.5) {
					ord.setPaid(ord.getPrice());
				} else {
					ord.setPaid((double) Math.round(Math.random() * 100.0 * ord.getPrice()) / 100);
				}
				OrderDAO.saveOrder(ord);
			}
		}
		Order ordt = new Order();
		ordt.setClient(test1);
		ordt.setProduct(p[0]);
		ordt.setCreatedBy(admins[0]);
		ofy().save().entity(ordt);
		return getAll();
	}
	
	@ApiMethod(path="getauth", httpMethod = "GET")
	public ArrayList<String> getAllAuth() {
		List<AuthSession> l = ofy().load().type(AuthSession.class).list();
		AuthContainer.reinit(l);
		return AuthContainer.testMethod();
	}

	@ApiMethod(path = "getAll", httpMethod = "GET")
	public List<String> getAll() {
		List<String> ret = new ArrayList<>();
		for (Class<?> clazz : UserAPI.objectifiedClasses) {
			for (Object o : ofy().load().type(clazz).list()) {
				ret.add(o.toString());
			}
		}
		ret.addAll(AuthContainer.testMethod());
		return ret;
	}

	@ApiMethod(path = "getAllObj", httpMethod = "GET")
	public List<Object> getAllObj() {
		List<Object> ret = new ArrayList<>();
		for (Class<?> clazz : UserAPI.objectifiedClasses) {
			for (Object o : ofy().load().kind(clazz.getSimpleName()).list()) {
				ret.add(o);
			}
		}
		ret.addAll(AuthContainer.testMethod());
		return ret;
	}

	@ApiMethod(path = "getCountry", httpMethod = "GET")
	public List<Object> getCountry() {
		return ofy().load().kind("Country").list();
	}

	private static class NameGen {
		public static ArrayList<Character> glasn = new ArrayList<>();

		static {
			char[] characters = { 'a', 'i', 'u', 'e', 'o', 'y' };
			for (char char_ : characters) {
				glasn.add(char_);
			}
		}

		public static String[] genNames(int size) {
			String[] names = new String[size];
			StringBuilder sb;
			double melodical = 0.33;
			double k = melodical;
			double kvar = 1.33;
			Random r = new Random();
			char temp;
			boolean glasnoe;

			for (int n = 0; n < size; n++) {
				sb = new StringBuilder();
				for (int i = 0; i < (Math.random() * 4) + 3; i++) {
					glasnoe = (Math.random() > k);
					do {
						temp = (char) (r.nextInt(('Z' - 'A' + 1)) + 'a');
					} while (checkGlasn(glasnoe, temp));
					k = ((glasnoe) ? k * kvar : k / kvar);
					if (i == 0) {
						temp = Character.toUpperCase(temp);
					}
					sb.append(temp);
				}
				names[n] = sb.toString();
			}
			return names;
		}

		private static boolean checkGlasn(boolean cond, char temp) {
			if (glasn.contains(temp) == cond) {
				return true;
			}
			return false;
		}
	}
}
