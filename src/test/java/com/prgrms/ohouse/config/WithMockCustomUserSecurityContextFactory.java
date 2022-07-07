package com.prgrms.ohouse.config;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.prgrms.ohouse.domain.common.security.JwtAuthentication;
import com.prgrms.ohouse.domain.common.security.JwtAuthenticationToken;
import com.prgrms.ohouse.domain.user.model.User;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

		final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		final User securityUser = User.builder()
			.nickname(annotation.nickname())
			.email(annotation.email())
			.password(annotation.password())
			.build();

		final JwtAuthenticationToken authenticationToken
			= new JwtAuthenticationToken(new JwtAuthentication("token", annotation.email()), null,
			Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
		authenticationToken.setDetails(securityUser);
		securityContext.setAuthentication(authenticationToken);

		return securityContext;
	}


}
