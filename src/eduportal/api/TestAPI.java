package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.entity.*;

@Api(name = "test", version = "v1")
public class TestAPI {
	
	
	
	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping () {
		return new Text("ping");
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
	
	@ApiMethod(path = "getAll", httpMethod = "GET")
	public List<Object> getAll () {
		return ofy().load().chunkAll().list();
		
	}
	
	@ApiMethod(path = "getCountry", httpMethod = "GET")
	public List<Object> getCountry () {
		return ofy().load().kind("Country").list();
		
	}
	
}
