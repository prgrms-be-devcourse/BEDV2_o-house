package com.prgrms.ohouse.domain.common.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.prgrms.ohouse.domain.user.model.User;

public class AuthUtils {

	public static User getAuthUser() {
		return (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

}
