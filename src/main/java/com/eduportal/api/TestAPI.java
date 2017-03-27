package com.eduportal.api;

import com.eduportal.AppLoader;
import com.eduportal.Text;
import com.eduportal.dao.GeoDAO;
import com.eduportal.dao.OrderDAO;
import com.eduportal.dao.ProductDAO;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.*;
import com.eduportal.manualdb.ConnectionHandler;
import com.eduportal.model.AuthContainer;
import com.eduportal.repo.*;
import com.mongodb.Block;
import com.eduportal.model.AccessSettings;
import com.eduportal.model.OrderLogic;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

@RestController
@Controller
@RequestMapping("/test")
public class TestAPI {

	@Autowired
	private UserRepository userRepository;// = AppLoader.get( UserRepository.class );
	@Autowired
	private ClientRepository clientRepository;// = AppLoader.get( ClientRepository.class );
	@Autowired
	private EmployeeRepository employeeRepository;// = AppLoader.get( EmployeeRepository.class );
	@Autowired
	private OrderRepository orderRepository;// = AppLoader.get( OrderRepository.class );
	@Autowired
	private AuthRepository authRepository;// = AppLoader.get( AuthRepository.class );

	@RequestMapping(path = "test", method = RequestMethod.GET)
	public List<Object> test() {
		ArrayList<Object> ret = new ArrayList<>();
		ArrayList<Object> re2t = new ArrayList<>();

		return re2t;
	}

	@RequestMapping(path = "test1", method = RequestMethod.GET)
	public List<Object> test1() {
		ArrayList<Object> ret = new ArrayList<>();
		UserEntity u = UserDAO.get("admin@corp.com", "pass");
		ret.add(u);
		ret.add(u.toString());
		return ret;
	}

	@RequestMapping(path = "cookies", method = RequestMethod.GET)
	public ArrayList<Object> testCookies(HttpServletRequest req) {
		ArrayList<Object> ret = new ArrayList<>();
		for (Cookie c : req.getCookies()) {
			ret.add(c.getName() + " " + c.getValue());
		}
		return ret;
	}

	@RequestMapping(name = "listSessions", path = "listsession", method = RequestMethod.GET)
	public List<String> listSession() {
		return AuthContainer.testMethod();
	}

	@RequestMapping(name = "ping", path = "ping", method = RequestMethod.GET)
	public Text ping() {
		return new Text("ping");
	}

	@RequestMapping(name = "Rebuild__DB", path = "rebuildDB", method = RequestMethod.GET)
	public List<String> rebuildDB(@RequestParam("size") String size) {
		ConnectionHandler.db().listCollections().forEach((Block<? super Document>) col -> {
			col.entrySet().forEach(ent -> {
				try {
					ConnectionHandler.db().getCollection(ent.getValue().toString()).deleteMany(new Document());
				} catch (Exception ignored) {
				}
			});
		});

		Employee[] admins = {
				new Employee("Adminovich", "Admin", "Adminov", "admin@corp.com", "pass", "+123456789012", new Date())
						.setAccessLevel(AccessSettings.ADMIN_LEVEL + 1),
				new Employee("Ordovich", "New", "Order", "order@corp.com", "order", "+123456789015", new Date())
						.setAccessLevel(AccessSettings.MODERATOR_LEVEL),
				new Employee("Superus", "Adminus", "Maximus", "adminus@corp.com", "adminus", "+123456789016",
						new Date()).setAccessLevel(AccessSettings.MODERATOR_LEVEL),
				new Employee("User", "User", "User", "user@corp.com", "user", "+123456789013", new Date())
						.setAccessLevel(AccessSettings.MODERATOR_LEVEL),
				new Employee("Wax", "John", "Doe", "john@doe.com", "johndoe", "+123456789014", new Date())
						.setAccessLevel(AccessSettings.MODERATOR_LEVEL)};
		for (Employee user : admins) {
			user.setCreator(admins[0].getId());
			user.setCorporation(AccessSettings.OWNERCORP_NAME);
			user.setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12)) + "-"
					+ (1 + (int) (Math.random() * 31)));
			user.addData("This is works", "YEAH");
		}
		ClientEntity[] clients;
		int dbsize;
		if (size == null) {
			size = "norm";
		}
		try {
			dbsize = Integer.parseInt(size);
		} catch (Exception e) {
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
					dbsize = 25;
					break;
			}
		}
		clients = new ClientEntity[dbsize];
		String[] clientName = NameGen.genNames(clients.length * 3);
		for (int i = 0; i < clients.length; i++) {
			clients[i] = new ClientEntity();
			clients[i].setName(clientName[3 * i]);
			clients[i].setSurname(clientName[3 * i + 1]);
			clients[i].setFathersname(clientName[3 * i] + "vych");
			clients[i].setPass("pass" + i);
			clients[i].setMail("user" + i + "@corp.com");
			clients[i].setPhone("+5555" + ((i < 10) ? "00" + i : (i > 9 && i < 100) ? "0" + i : i) + "12345");
			clients[i].setCreator(admins[i % admins.length].getId());
			clients[i].defineCurator(admins[i % admins.length]);
			clients[i].setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12)) + "-"
					+ (1 + (int) (Math.random() * 31)));
			clients[i].addData("Адрес Skype", "skuser" + i);
			clients[i].addData("Год окончания обучения", (2010 + (int) (Math.random() * 7)) + "");
			if (Math.random() < 0.7) {
				for (String str : ClientEntity.userParams) {
					StringBuilder sb = new StringBuilder();
					for (String nnn : NameGen.genNames((int) (Math.random() * 10) + 1)) {
						sb.append(nnn);
					}
					clients[i].addData(str, sb.toString());
				}
				clients[i].setActive(true);
			}
		}

		Employee agents[] = new Employee[15];
		clientName = NameGen.genNames(agents.length * 3);
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new Employee();
			agents[i].setCorporation(
					(i < 5) ? AccessSettings.OWNERCORP_NAME : "AgentCorp" + (int) (Math.random() * 2 + 1));
			agents[i].setName(clientName[3 * i]);
			agents[i].setSurname(clientName[3 * i + 1]);
			agents[i].setFathersname(clientName[3 * i] + "vych");
			agents[i].setPass("pass" + i);
			agents[i].setMail("user" + i + "@agent.com");
			agents[i].setPhone("+7555" + ((i < 10) ? "00" + i : (i > 9 && i < 100) ? "0" + i : i) + "12345");
			agents[i].setCreator(admins[i % admins.length].getId());
			agents[i].setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12)) + "-"
					+ (1 + (int) (Math.random() * 31)));
			agents[i].setAgent(true);
		}

		// Test testovich
		ClientEntity test1 = new ClientEntity("Testovich", "Test", "Testov", "test@qwe.rty", "test", "+123455556789",
				new Date());
		test1.setBorn("" + (1950 + (int) (Math.random() * 60)) + "-" + (1 + (int) (Math.random() * 12)) + "-"
				+ (1 + (int) (Math.random() * 31)));
		test1.setActive(true);
		for (String str : ClientEntity.userParams) {
			StringBuilder sb = new StringBuilder();
			for (String nnn : NameGen.genNames((int) (Math.random() * 10) + 1)) {
				sb.append(nnn);
			}
			test1.addData(str, sb.toString());
		}
		test1.setCreator(admins[0].getId());
		test1.defineCurator(admins[0]);
		test1.setActive(true);
		userRepository.save(test1);
		clientRepository.save(test1);

		Arrays.stream(admins).parallel().peek(userRepository::save).forEach(employeeRepository::save);
		Arrays.stream(agents).parallel().peek(userRepository::save).forEach(employeeRepository::save);
		Arrays.stream(clients).parallel().peek(userRepository::save).forEach(clientRepository::save);

		// End creating users

		City[] c = {GeoDAO.createCity("Kiev", "Ukraine"), GeoDAO.createCity("Lvov", "Ukraine"),
				GeoDAO.createCity("Prague", "Czech Republic"), GeoDAO.createCity("Budapest", "Hungary"),
				GeoDAO.createCity("London", "United Kingdom")};
		Product p[] = {new Product("Test product", "Some product to test", c[0]),
				new Product("NewOrderTest", "Some product to test new order", c[1]),
				new Product("Prague Study School", "Nice Prague school of english, .......", c[2]),
				new Product("Высшая школа Будапешта", "Описание программы", c[3]),
				new Product("Высшая школа Лондона", "Описание программы", c[4]),
				new Product("LSE", "Описание программы", c[4]), new Product("Ещё один ВУЗ", "Описание программы", c[2]),
				new Product("КПИ", "Как же без него?", c[0]),};
		for (Product prod : p) {
			prod.setActual(Math.random() < 0.75); // chance vary!
			prod.setDefaultPrice((double) Math.round(Math.random() * 100_000_00) / 100);
			prod.setProvider((Math.random() < 0.66) ? AccessSettings.OWNERCORP_NAME : "GlobalEdu");
			prod.setCurrency("UAH");
		}
		ProductDAO.save(p);
		Random r = new Random();
		for (ClientEntity cli : clients) {
			if (cli.isActive()) {
				Order ord = null;
				if (Math.random() > 0.5) {
					ord = OrderLogic.createOrder(cli, cli.getCurator(), p[r.nextInt(p.length)], 0.0, "", null, null);
					if (Math.random() > 0.5) {
						ord.setPaid(ord.getPrice());
					} else {
						ord.setPaid((double) Math.round(Math.random() * 100.0 * ord.getPrice()) / 100);
					}
				} else {
					ord = OrderLogic.createOrder(cli, agents[new Random().nextInt(agents.length)],
							p[r.nextInt(p.length)], 0.0, "", null, null);
				}

				OrderDAO.saveOrder(ord);
			}
		}
		Order ordt = new Order();
		ordt.setClientId(test1.getId());
		ordt.setProductId(p[0].getId());
		ordt.setCreatedById(admins[0].getId());
		ordt.defineCurator(admins[0]);
		orderRepository.save(ordt);

		return getAll();
	}

	@RequestMapping(path = "/getMappings")
	public ResponseEntity getMappings() {
		return ResponseEntity.ok(
				Stream.of(AdminAPI.class, ModeratorAPI.class, OrderAPI.class, TestAPI.class, UserAPI.class)
						.flatMap(clz -> stream(clz.getMethods()))
						.filter(e -> e.getAnnotations().length > 0)
						.filter((Method e) -> stream(e.getAnnotations())
								.map(x -> x.annotationType().getSimpleName())
								.anyMatch(x -> x.equals("RequestMapping") || x.equals("GetMapping")))
						.map((Method meth) -> stream(meth.getDeclaringClass().getAnnotations())
								.filter(x -> x.annotationType().getSimpleName().equals("RequestMapping"))
								.map(x -> (RequestMapping) x)
								.map(RequestMapping::value)
								.map(arr -> (arr.length == 1) ? arr[0] : Arrays.toString(arr))
								.findAny()
								.orElse("Nothing")
								+ "/" +
								((meth.getAnnotation(RequestMapping.class) != null && meth.getAnnotation(RequestMapping.class).path().length == 1)
										? meth.getAnnotation(RequestMapping.class).path()[0]
										: ((meth.getAnnotation(GetMapping.class) != null)
										? ((meth.getAnnotation(GetMapping.class).path().length == 1)
										? meth.getAnnotation(GetMapping.class).path()[0]
										: Arrays.toString(meth.getAnnotation(GetMapping.class).path()))
										: Arrays.toString(meth.getAnnotation(RequestMapping.class).path()))
								)
								+ " :: "
								+ stream(meth.getParameterAnnotations())
								.flatMap(Arrays::stream)
								.filter(x -> x.annotationType().getSimpleName().equals("RequestParam"))
								.map(x -> (RequestParam) x)
								.map(RequestParam::value)
								.reduce((s1, s2) -> s1 + ", " + s2)
								.orElse("-----"))
						.collect(Collectors.toList()));
	}

	@RequestMapping(path = "getauth", method = RequestMethod.GET)
	public ArrayList<String> getAllAuth() {
		List<AuthSession> l = authRepository.findAll();
		AuthContainer.reinit(l);
		return AuthContainer.testMethod();
	}

	@RequestMapping(path = "getAll", method = RequestMethod.GET)
	public List<String> getAll() {
		List<String> ret = new ArrayList<>();
		for (Object o : getAllObj()) {
			ret.add(o.toString());
		}
		ret.addAll(AuthContainer.testMethod());
		return ret;
	}

	@RequestMapping(path = "getAllObj", method = RequestMethod.GET)
	public List<Object> getAllObj() {

		List<Object> ret = new ArrayList<>();
		MongoRepository[] repos = {userRepository, clientRepository, employeeRepository, orderRepository,
				AppLoader.get(ProductRepository.class)
		};
		for (MongoRepository repo : repos) {
			ret.addAll(repo.findAll());
		}

		ret.addAll(AuthContainer.testMethod());
		return ret;
	}

	private static class NameGen {
		public static ArrayList<Character> glasn = new ArrayList<>();

		static {
			char[] characters = {'a', 'i', 'u', 'e', 'o', 'y'};
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
