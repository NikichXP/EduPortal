package com.eduportal.model;

public class AccessSettings {
	
	public static final int EVERYONE = 0;
	public static final int MODERATOR_LEVEL = 10;
	public static final int ADMIN_LEVEL = 0xBACC;
	
	public static final String[] ACCESSNAMES = {"User", "Moderator", "Admin"};
	public static final int[] ACCESSVALUES = {EVERYONE, MODERATOR_LEVEL, ADMIN_LEVEL};
	public static final String DEFAULT_COUNTRY = "Ukraine";
	
	public static final String OWNERCORP_NAME = "Vedi Tour Group";
	
	public static final boolean ALLOW_USER_MULTISESSIONS = false;
	public static final boolean ALLOW_WORKER_MULTISESSIONS = false;
	public static final long USER_SESSION_TIMEOUT = 60 * 60 * 1000;
	public static final long WORKER_SESSION_TIMEOUT = 12 * 60 * 60 * 1000;
	
	public static final int CREATE_USER = MODERATOR_LEVEL;
	public static final int EDIT_ORDER = MODERATOR_LEVEL;
	public static final int LIST_TOKENS = ADMIN_LEVEL;
	public static final int LIST_USERS = MODERATOR_LEVEL;
	public static final int LIST_OFFLINE_PRODUCTS = MODERATOR_LEVEL - 1;
	public static final int LIST_ACTUAL_PRODUCTS = EVERYONE;
	public static final int DEACTIVATE_PRODUCTS = MODERATOR_LEVEL;
	public static final int CREATE_CITY = ADMIN_LEVEL;
	public static final int LIST_ALL_ORDERS = ADMIN_LEVEL;
	public static final int CANCEL_ORDER = EDIT_ORDER;
	public static final int CREATE_ORDER = MODERATOR_LEVEL;
}
