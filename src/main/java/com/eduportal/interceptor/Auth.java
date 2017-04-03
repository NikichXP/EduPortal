package com.eduportal.interceptor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {

	Types[] value() default {};

	public static enum Types {
		MODERATOR("MODERATOR"), ADMIN("ADMIN"), WORKER("WORKER"), ANY("ANY"), CLIENT("CLIENT"), SELF("SELF"), MANAGED("MANAGED");

		final String id;
		Types(String id) { this.id = id; }
		public String getValue() { return id; }
	}

	public static @interface Param {
		String value() default "";
	}

}
