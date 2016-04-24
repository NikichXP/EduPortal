package eduportal.api;

import com.google.api.server.spi.config.*;

import eduportal.dao.UserDAO;
import eduportal.dao.entity.UserEntity;
import eduportal.model.AuthContainer;

@Api(name = "admin", title = "Admin API")
public class AdminAPI {

	@ApiMethod (name = "promote", httpMethod = "GET", path = "promote")
	public UserEntity promoteUser (@Named("token") String token, @Named("target") String target, @Named("access") String access) {
		if (AuthContainer.getAccessGroup(token) != 0xBACC) {
			return null;
		}
		UserEntity u = UserDAO.get(target);
		if (u == null) {
			return u;
		}
		try {
		u.setAccessGroup(Integer.parseInt(access));
		} catch (Exception e) {
			return null;
		}
		UserDAO.update(u);
		u.setPass(null);
		return u;
	}
	
}
