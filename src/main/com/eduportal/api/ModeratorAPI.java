package com.eduportal.api;

import com.eduportal.Text;
import com.eduportal.dao.GeoDAO;
import com.eduportal.dao.OrderDAO;
import com.eduportal.dao.ProductDAO;
import com.eduportal.dao.UserDAO;
import com.eduportal.entity.*;
import com.eduportal.model.AccessLogic;
import com.eduportal.model.AuthContainer;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moderator")
public class ModeratorAPI {
	
	@RequestMapping (path = "myClients", method = RequestMethod.GET)
	public List<ClientEntity> listMyUsers(@RequestParam("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return UserDAO.getClients(emp);
	}
	
	@RequestMapping (path = "myOrders", method = RequestMethod.GET)
	public List<Order> listOrders (@RequestParam("token") String token) {
		Employee emp = AuthContainer.getEmp(token);
		if (emp == null) {
			return null;
		}
		return OrderDAO.getOrdersByUser(emp);
	}

	@RequestMapping(path = "listProducts", method = RequestMethod.GET)
	public List<Product> listProducts(@RequestParam("token") String token) {
		if (AccessLogic.canSeeAllProducts(token) == false) {
			return null;
		}
		return ProductDAO.getAll();
	}

	@RequestMapping(name = "addProduct", method = RequestMethod.GET, path = "product/add")
	public Text addProduct(@RequestParam("title") String title, @RequestParam("description") String descr,
	                       @RequestParam("cityname") String cityname, @RequestParam("token") String token, @RequestParam("price") Double price,
	                       @RequestParam("begin") String begin) {
		if (!AccessLogic.canAddProduct(token).equals("GOOD")) {
			return new Text("403 Forbidden " + AccessLogic.canAddProduct(token));
		}
		City city = GeoDAO.getCity(cityname);
		if (city == null) {
			return new Text("No such city exist");
		}

		Product p = new Product(title, descr, city);
		p.setDefaultPrice(price);
		ProductDAO.save(p);
		return new Text(p.toString());
	}

	@RequestMapping(name = "editProduct", method = RequestMethod.GET, path = "product/edit")
	public Text editProduct(@RequestParam("id") String id, @RequestParam("title") String title, @RequestParam("description") String descr,
			@RequestParam("cityid") String cityname, @RequestParam("token") String token, @RequestParam("price") Double price,
			@RequestParam("begin") String begin) {
		if (!AccessLogic.canAddProduct(token).equals("GOOD")) {
			return new Text("403 Forbidden " + AccessLogic.canAddProduct(token));
		}
		Product p = ProductDAO.get(id);
		if (p == null) {
			return new Text("Product " + id + " not found");
		}
		if (cityname != null) {
			if (GeoDAO.getCity(cityname) != null) {
				p.defineCity(GeoDAO.getCity(cityname));
			} else {
				return new Text("City " + cityname + " not found");
			}
		}
		p.updateIfNotNull(title, descr, price);
		ProductDAO.save(p);
		return new Text(p.toString());
	}
}
