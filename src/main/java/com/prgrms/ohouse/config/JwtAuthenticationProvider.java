package com.prgrms.ohouse.config;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken)authentication;
		return processUserAuthentication(
			String.valueOf(jwtAuth.getPrincipal()), jwtAuth.getCredentials()
		);
	}

	private Authentication processUserAuthentication(String principal, String credentials) {
		User user = userService.login(new UserLoginCommand(principal, credentials));
		List<GrantedAuthority> authorities = (List<GrantedAuthority>)user.getAuthorities();
		String token = jwtTokenProvider.createToken(user.getEmail(), authorities);
		JwtAuthenticationToken authenticatedToken = new JwtAuthenticationToken(
			new JwtAuthentication(token, user.getEmail()), null, authorities);
		authenticatedToken.setDetails(user);
		return authenticatedToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
