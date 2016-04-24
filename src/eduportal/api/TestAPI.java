package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import eduportal.dao.entity.*;

@Api(name = "test", version = "v1")
public class TestAPI {
	
	static {
		ObjectifyService.begin();
		ObjectifyService.register(UserEntity.class);
	}
	
	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping () {
		return new Text("ping");
	}
	
	
	@ApiMethod(name = "user.getAll", path = "listUser", httpMethod = "GET")
	public List<Object> listUsers() {
		return ofy().load().kind("UserEntity").list();
	}
	
}
