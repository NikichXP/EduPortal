package eduportal.model;

import eduportal.dao.entity.Order;
import eduportal.dao.entity.UserEntity;

public class AccessLogic {

	public static boolean canEditOrder(UserEntity admin, Order order) {
		if (admin.accessGroup() < AccessSettings.EDIT_ORDER) {
			return false;
		}
		if (admin.getAccessLevel().corporationEntity().getId() == AccessSettings.OWNERCORP.getId()) {
			return true;
		}
		UserEntity creator = order.createdByEntity();
		if (creator.getAccessLevel().getCorporation() == admin.getAccessLevel().getCorporation()) {
			return true;
		}
		return false;
	}

	public static boolean canCreateUser(UserEntity u) {
		if (u.accessGroup() < AccessSettings.CREATE_USER) {
			return false;
		}
		return true;
	}

	public static boolean canSeeTokens(String token) {
		if (AuthContainer.checkReq(token, AccessSettings.SEE_TOKENS)) {
			return true;
		}
		return false;
	}
	
}
