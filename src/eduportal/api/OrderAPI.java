package eduportal.api;

import java.util.*;
import com.google.api.server.spi.config.*;
import eduportal.dao.OrderDAO;
import eduportal.dao.entity.Order;

@Api(name = "order", version = "v1")
public class OrderAPI {

	@ApiMethod(name = "getAll", path = "get/all", httpMethod = "GET") 
	public List<Order> getAllOrders () {
		return OrderDAO.getAll();
	}
	
	
}
