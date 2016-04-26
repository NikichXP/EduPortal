package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import com.googlecode.objectify.ObjectifyService;
import eduportal.dao.entity.*;
import eduportal.model.AuthContainer;

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
	
	@ApiMethod (name = "listSessions", path = "list/session", httpMethod = "GET")
	public List<String> listSession () {
		return AuthContainer.testMethod();
	}
	
	@ApiMethod(name = "user.getAll", path = "list/user", httpMethod = "GET")
	public List<Object> listUsers() {
		return ofy().load().kind("UserEntity").list();
	}
	
	@ApiMethod (name = "Rebuild_user_DB", path = "rebuildDB", httpMethod = "GET")
	public UserEntity[] rebuildDB() {
		for (Object u : ofy().load().kind("UserEntity").list()) {
			ofy().delete().type(UserEntity.class).id(((UserEntity) u).getId()).now();
		}
		UserEntity[] users = {
				new UserEntity("admin", "pass", "Admin", "Adminov").setAccessGroupR(0xBACC),
				new UserEntity("user", "user", "User", "User")
		};
		ofy().save().entities(users);
		return users;
	}
	
}
