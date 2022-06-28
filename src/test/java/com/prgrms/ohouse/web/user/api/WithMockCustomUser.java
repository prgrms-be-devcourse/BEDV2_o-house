package com.prgrms.ohouse.web.user.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
	String email() default "guest@gmail.com";

	String password() default "guestPassword12";

	String nickname() default "guest";
}
