package eduportal.api;

import static com.googlecode.objectify.ObjectifyService.ofy;
import java.util.*;
import com.google.api.server.spi.config.*;
import com.google.appengine.api.datastore.Text;
import eduportal.dao.entity.*;
import eduportal.model.*;

@Api(name = "test", version = "v1")
public class TestAPI {

	@ApiMethod(name = "ping", path = "ping", httpMethod = "GET")
	public Text ping() {
		return new Text("ping");
	}

	@ApiMethod(name = "Rebuild_user_DB", path = "rebuildDB", httpMethod = "GET")
	public UserEntity[] rebuildDB() {
		for (Object u : ofy().load().kind("UserEntity").list()) {
			ofy().delete().type(UserEntity.class).id(((UserEntity) u).getId()).now();
		}
		UserEntity[] users = {
				new UserEntity("admin", "pass", "Admin", "Adminov", "+123456789012", "mail@me.now")
						.setAccessGroupR(AccessSettings.ADMIN_LEVEL + 1),
				new UserEntity("user", "user", "User", "User", "+123456789013", "mail@me2.now") };
		ofy().save().entities(users);
		return users;
	}
	
	@ApiMethod(path = "getAll", httpMethod = "GET")
	public List<Object> getAll() {
		return ofy().load().chunkAll().list();

	}

	@ApiMethod(path = "getCountry", httpMethod = "GET")
	public List<Object> getCountry() {
		return ofy().load().kind("Country").list();

	}

}
