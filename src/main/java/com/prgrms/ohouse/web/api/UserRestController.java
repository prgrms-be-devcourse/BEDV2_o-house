package com.prgrms.ohouse.web.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.config.JwtAuthentication;
import com.prgrms.ohouse.config.JwtAuthenticationProvider;
import com.prgrms.ohouse.config.JwtAuthenticationToken;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.web.requests.UserCreateRequest;
import com.prgrms.ohouse.web.requests.UserLoginRequest;
import com.prgrms.ohouse.web.results.ApiResult;
import com.prgrms.ohouse.web.results.UserCreateResult;
import com.prgrms.ohouse.web.results.UserLoginResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

	private final UserService userService;
	private final JwtAuthenticationProvider authenticationProvider;

	@PostMapping("/signUp")
	public ResponseEntity<ApiResult> signUp(@RequestBody @Valid UserCreateRequest request, Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException();
		}
		userService.signUp(request.toCommand());
		return UserCreateResult.build();
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResult> login(@RequestBody @Valid UserLoginRequest request, Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException();
		}
		JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
		Authentication auth = authenticationProvider.authenticate(authToken);
		JwtAuthentication jwtAuthentication = (JwtAuthentication)auth.getPrincipal();
		return UserLoginResult.build(jwtAuthentication.token);
	}
}
