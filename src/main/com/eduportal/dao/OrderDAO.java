package com.eduportal.dao;

import com.eduportal.entity.Order;
import com.eduportal.entity.Product;
import com.eduportal.entity.UserEntity;
import com.eduportal.repo.OrderRepository;
import com.eduportal.AppLoader;
import com.eduportal.entity.City;
import com.eduportal.repo.ProductRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.stream;

public class OrderDAO {

	private static OrderRepository orderRepo;
	private static ProductRepository prodRepo;

	private static ProductRepository prodRepo() {
		if (prodRepo == null) {
			prodRepo = AppLoader.get(ProductRepository.class);
		}
		return prodRepo;
	}

	private static OrderRepository orderRepo() {
		if (orderRepo == null) {
			orderRepo = AppLoader.get(OrderRepository.class);
		}
		return orderRepo;
	}

	public static List<Order> getOrdersByUser(UserEntity u) {
		return orderRepo().findMyOrders(u.getId());
	}

	public static List<Product> getProductsByCompany(String corp) {
		return prodRepo().findByProvider(corp);
	}

	public static List<Order> getOrdersByProduct(String prod) {
		return orderRepo().findByProductId(prod);
	}

	public static List<Order> getOrdersByProduct(Product prod) {
		return getOrdersByProduct(prod.getId());
	}

	public static List<Order> getNoCuratorOrders() {
		return orderRepo().findByCurator(null); //no curator
	}

	public static List<Order> getOrdersCreatedByUser(UserEntity u) {
		return orderRepo().findByCreator(u.getId());
	}

	public static List<Order> getSelfOrdersByUser(UserEntity u) {
		return orderRepo().findByClient(u.getId());
	}

	public static List<Order> getAllOrders() {
		return orderRepo().findAll();
	}

	public static List<Product> getAllProducts(boolean isActual) {
		return prodRepo().findAll();
	}

	public static void saveOrder(Order o) {
		try {
			if (o.getStart() == null) {
				o.setStart(new Date());
			}
			if (o.getEnd() == null) {
				o.setEnd(new Date(System.currentTimeMillis() + (1_000 * 3600)));
			}
		} catch (NullPointerException e) {
			if (o == null) {
				throw new IllegalArgumentException();
			}
			o.setStart(new Date());
			o.setEnd(new Date(System.currentTimeMillis() + (1_000 * 3600)));
		}
		orderRepo().save(o);
	}

	public static void saveProduct(Product p) {
		prodRepo().save(p);
	}

	public static boolean saveProduct(String title, String description, City c, double defaultPrice) {
		Product p = new Product(title, description, c).setDefaultPrice(defaultPrice);
		prodRepo().save(p);
		return true;
	}

	public static Product getProduct(String product) {
		return prodRepo().findOne(product);
	}

	public static Order getOrder(String id) {
		return orderRepo().findOne(id);
	}

	public static void deleteOrder(Order order) {
		orderRepo().delete(order.getId());
	}

	public static List<Order> filterOrders(String clientName, String clientId, Boolean isPaid, String createdBy) throws InvocationTargetException, IllegalAccessException {
		Map<String, Object> args = new HashMap<>();
		if (clientId != null) {
			args.put("clientId", clientId);
		}
		if (isPaid != null) {
			args.put("donePaid", isPaid);
		}
		if (createdBy != null) {
			args.put("createdById", createdBy);
		}
		Method method = stream(prodRepo().getClass().getDeclaredMethods())
				.filter(meth -> meth.getName().equals("listCustom" + args.size() + "ArgQuery"))
				.findFirst()
				.orElseGet(() -> null);

		Object[] queryArgs = new Object[args.size() * 2];
		int ptr = 0;
		for (Object o : args.keySet()) {
			queryArgs[ptr] = o;
			queryArgs[ptr + 1] = args.get(o);
			ptr += 2;
		}

		return (List<Order>) method.invoke(prodRepo(), queryArgs);
	}
}
