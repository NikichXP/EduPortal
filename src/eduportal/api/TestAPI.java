package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;

import javax.inject.Inject;
import javax.servlet.http.*;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.*;
import eduportal.dao.entity.*;
import eduportal.model.*;
import com.googlecode.objectify.*;

@Api(name = "test", version = "v1", auth = @ApiAuth(allowCookieAuth = AnnotationBoolean.TRUE))
public class TestAPI {
	
	@Inject
	private static AuthContainer auth;

	@ApiMethod(path = "test", httpMethod = "GET")
	public ArrayList<Object> test(HttpServletRequest req) {
		ArrayList<Object> ret = new ArrayList<>();
		for (String s : auth.testMethod()) {
			ret.add(s);
		}
		ret.add("end");
		return ret;
	}

	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping() {
		return new Text("ping");
	}

	@ApiMethod(name = "Rebuild__DB", path = "rebuildDB", httpMethod = "GET")
	public List<String> rebuildDB(@Named("size") String size) {
		ofy().cache(false).flush();
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object u : ofy().load().kind(clazz.getSimpleName()).list()) {
				ofy().delete().entity(u).now();
			}
		}
		UserEntity[] users = {
				new UserEntity("admin", "pass", "Admin", "Adminov", "+123456789012", "mail@me.now")
						.setAccessGroup(AccessSettings.ADMIN_LEVEL + 1),
				new UserEntity("order", "order", "New", "Order", "+123456789015", "kelly@neworder.org")
						.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL),
				new UserEntity("adminus", "adminus", "Adminus", "Maximus", "+123456789016", "virto@asus.com")
						.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL),
				new UserEntity("user", "user", "User", "User", "+123456789013", "mail@me2.now")
						.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL),
				new UserEntity("johndoe", "johndoe", "John", "Doe", "+123456789014", "john@doe.bar")
						.setAccessGroup(AccessSettings.MIN_MODERATOR_LVL) };
		for (UserEntity user : users) {
			user.setCreator(users[0]);
		}
		UserEntity[] clients = new UserEntity[100];
		String[] clientName = NameGen.genNames(clients.length * 2);
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new UserEntity("user" + i, "pass" + i, clientName[2 * i], clientName[2 * i + 1],
					"+5555" + ((i < 10) ? "00" + i : (i > 9 && i < 100) ? "0" + i : i) + "12345",
					(clientName[2 * i] + "@" + clientName[2 * i + 1] + ".nomm").toLowerCase()).setCreator(users[i%5]);
		}
		ofy().save().entities(users);
		ofy().save().entities(clients);
		City[] c = { GeoDAO.createCity("Kiev", "Ukraine"), GeoDAO.createCity("Lvov", "Ukraine"),
				GeoDAO.createCity("Prague", "Czech Republic"), GeoDAO.createCity("Budapest", "Hungary"),
				GeoDAO.createCity("London", "United Kingdom") };
		Product p[] = { new Product("Test product", "Some product to test", c[0]),
				new Product("NewOrderTest", "Some product to test new order", c[1]),
				new Product("Prague Study School", "Nice Prague school of english, .......", c[2]),
				new Product("Высшая школа Будапешта", "Описание программы", c[3]),
				new Product("Высшая школа Лондона", "Описание программы", c[4]),
				new Product("LSE", "Описание программы", c[4]), new Product("Ещё один ВУЗ", "Описание программы", c[2]),
				new Product("КПИ", "Как же без него?", c[0]), };

		ProductDAO.save(p);
		int ordersize;
		switch (size) {
		case "big":
			ordersize = 100;
			break;
		case "large":
			ordersize = 500;
			break;
		case "ultra":
			ordersize = 1_000;
			break;
		case "mega":
			ordersize = 10_000;
			break;
		case "fuck":
			ordersize = 512_000;
			break;
		default:
			ordersize = 20;
			break;
		}
		Order o[] = new Order[ordersize];
		for (int ptr = 0; ptr < o.length; ptr++) {
			o[ptr] = new Order();
		}

		int i = 0;
		for (Order ord : o) {
			i++;
			ord.setUser(clients[i % clients.length]);
			ord.setCreatedBy(users[i % 3]);
			ord.setProduct(p[i % p.length]);
			ord.setPrice((double) Math.round(Math.random() * 10_000_00) / 100);
			if (Math.random() > 0.5) {
				ord.setPaid(ord.getPrice());
			} else {
				ord.setPaid((double) Math.round(Math.random() * 10 * ord.getPrice()) / 100);
			}
			OrderDAO.saveOrder(ord);
		}
		return getAll();
	}

	@ApiMethod(path = "getAll", httpMethod = "GET")
	public List<String> getAll() {
		List<String> ret = new ArrayList<>();
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object o : ofy().load().kind(clazz.getSimpleName()).list()) {
				ret.add(o.toString());
			}
		}
		ret.addAll(auth.testMethod());
		return ret;
	}
	
	@ApiMethod(path = "getAllObj", httpMethod = "GET")
	public List<Object> getAllObj() {
		List<Object> ret = new ArrayList<>();
		for (Class<?> clazz : AdminAPI.objectifiedClasses) {
			for (Object o : ofy().load().kind(clazz.getSimpleName()).list()) {
				ret.add(o);
			}
		}
		ret.addAll(auth.testMethod());
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
