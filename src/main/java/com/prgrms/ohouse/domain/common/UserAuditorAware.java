package com.prgrms.ohouse.domain.common;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.common.security.AuthUtils;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserAuditorAware implements AuditorAware<User> {
	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.of(AuthUtils.getAuthUser());

	}
}