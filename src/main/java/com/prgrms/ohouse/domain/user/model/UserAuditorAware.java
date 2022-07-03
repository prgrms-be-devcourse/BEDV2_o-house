package com.prgrms.ohouse.domain.user.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.common.security.AuthUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserAuditorAware implements AuditorAware<User> {

	@Autowired
	AuthUtility authUtility;

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.of(authUtility.getAuthUser());

	}
}