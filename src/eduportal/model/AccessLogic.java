package eduportal.model;

import java.util.List;

import eduportal.dao.UserDAO;
import eduportal.dao.entity.Employee;
import eduportal.dao.entity.Order;
import eduportal.dao.entity.UserEntity;

public class AccessLogic {

	public static boolean canEditOrder(UserEntity admin, Order order) {
		if (admin instanceof Employee == false) {
			return false;
		}
		Employee emp = (Employee) admin;
		if (emp.getAccessLevel() < AccessSettings.EDIT_ORDER) {
			return false;
		}
		if (emp.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			return true;
		}
		Employee creator = order.createdByEntity();
		if (creator.getCorporation().equals(emp.getCorporation())) {
			return true;
		}
		return false;
	}

	public static boolean canCreateUser(UserEntity u) {
		if (u instanceof Employee == false) {
			return false;
		}
		if (((Employee)u).getAccessLevel() < AccessSettings.CREATE_USER) {
			return false;
		}
		return true;
	}

	public static boolean canSeeTokens(String token) {
		if (AuthContainer.checkReq(token, AccessSettings.LIST_TOKENS)) {
			return true;
		}
		return false;
	}

	public static boolean canListAllUsers(String token) {
		UserEntity tuser = AuthContainer.getUser(token);
		if (tuser instanceof Employee == false) {
			return false;
		}
		Employee user = (Employee) tuser;
		if (user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			if (user.getAccessLevel() >= AccessSettings.LIST_USERS) {
				return true;
			}
		}
		return false;
	}

	public static List<UserEntity> listUsers(String phone, String name, String mail, String login, String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getAccessLevel() < AccessSettings.LIST_USERS) {
			return null;
		}
		return UserDAO.searchUsers(phone, name, mail, user.getCorporation());
	}

	public static boolean canSeeAllProducts(String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			if (user.getAccessLevel() >= AccessSettings.LIST_OFFLINE_PRODUCTS) {
				return true;
			}
		}
		return false;
	}

	public static boolean canSeeProducts(String token) {
		
		//EVERYONE CAN. IF NOT - RECOMMENT THIS
		
//		if (AuthContainer.getEmp(token).getAccessLevel() >= AccessSettings.LIST_ACTUAL_PRODUCTS) {
//			return true;
//		}
//		return false;
		if (AuthContainer.getUser(token) != null) {
			return true;
		}
		return false;
	}

	public static boolean canActivateProduct(String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			if (user.getAccessLevel() >= AccessSettings.DEACTIVATE_PRODUCTS) {
				return true;
			}
		}
		return false;
	}

	public static boolean canCreateCity(String token) {
		if (AuthContainer.getEmp(token).getAccessLevel() >= AccessSettings.CREATE_CITY) {
			return true;
		}
		return false;
	}

	public static String canAddProduct(String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			if (user.getAccessLevel() >= AccessSettings.DEACTIVATE_PRODUCTS) {
				return "GOOD";
			}
			return "Access";
		}
		return "Corp";
	}

	public static boolean canSeeAllOrders(String token) {
		Employee user = AuthContainer.getEmp(token);
		if (user.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			if (user.getAccessLevel() >= AccessSettings.LIST_ALL_ORDERS) {
				return true;
			}
		}
		return false;
	}

	public static boolean canCancelOrder(String token, Order order) {
		if (!AuthContainer.checkReq(token, AccessSettings.CANCEL_ORDER)) {
			return false;
		}
		Employee admin = AuthContainer.getEmp(token);
		if (admin.getCorporation().equals(AccessSettings.OWNERCORP_NAME)) {
			return true;
		}
		Employee creator = order.createdByEntity();
		if (creator.getCorporation().equals(admin.getCorporation())) {
			return true;
		}
		return false;
	}

	public static boolean canCreateOrder(Employee admin) {
		if (admin.getAccessLevel() >= AccessSettings.CREATE_ORDER) {
			return true;
		}
		return false;
	}
	
	public static boolean canAccessAdminPanel (Employee user) {
		if (user == null) {
			return false;
		}
		if (user.getAccessLevel() >= AccessSettings.ADMIN_LEVEL) {
			return true;
		}
		return false;
	}

	public static boolean canEditUser(UserEntity admin, UserEntity user) {
		if (admin.equals(user)) {
			return true;
		}
		Employee emp = (Employee) admin;
		if (emp.getAccessLevel() < AccessSettings.CREATE_USER) {
			return false;
		}
		if (emp.getCorporation().equals(AccessSettings.OWNERCORP_NAME) == false) {
			//TODO Work here
				return false;
			
		}
		return true;
	} 
}
