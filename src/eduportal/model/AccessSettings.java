package eduportal.model;

import eduportal.dao.UserDAO;
import eduportal.dao.entity.Corporation;

public class AccessSettings {
	
	public static final int MODERATOR_LEVEL = 10;
	public static final int ADMIN_LEVEL = 0xBACC;
	
	public static final Corporation OWNERCORP = UserDAO.getCorp("Vedi Tour Group");
	
	public static final int CREATE_USER = MODERATOR_LEVEL;
	public static final int EDIT_ORDER = MODERATOR_LEVEL;
	public static final int SEE_TOKENS = ADMIN_LEVEL;
	
}
