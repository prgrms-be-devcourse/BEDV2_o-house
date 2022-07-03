package com.prgrms.ohouse.domain.common.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.user.model.User;

@Component
public class SpringSecurityAuthUtility implements AuthUtility {
	@Override
	public User getAuthUser() {
		return (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
}
