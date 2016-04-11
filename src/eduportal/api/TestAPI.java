package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.ObjectifyService;

import eduportal.dao.UserDAO;
import eduportal.dao.entity.*;
import eduportal.utils.Text;

@Api(name = "test", version = "v1")
public class TestAPI {
	
	static {
		ObjectifyService.begin();
		ObjectifyService.register(UserEntity.class);
		System.out.println("reg complete");
	}
	
	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping () {
		return new Text("ping");
	}
	
	@ApiMethod (name = "register", httpMethod = "GET", path="register")
	public Text register (@Named ("name") String name, @Named ("pass") String pass, @Named ("login") String login, @Named ("surname") String surname) {
		UserEntity u = UserDAO.create(login, pass, name, surname);
		return new Text ("done creating " + u.getId() + u.getName());
	}
	
	@ApiMethod(name = "user.getAll", path = "listUser", httpMethod = "GET")
	public List<Object> listUsers() {
		return ofy().load().kind("UserEntity").list();
	}
}
