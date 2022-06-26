package com.prgrms.ohouse.web.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.common.security.JwtAuthentication;
import com.prgrms.ohouse.domain.common.security.JwtAuthenticationProvider;
import com.prgrms.ohouse.domain.common.security.JwtAuthenticationToken;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.utils.AuthUtils;
import com.prgrms.ohouse.web.requests.UserCreateRequest;
import com.prgrms.ohouse.web.requests.UserLoginRequest;
import com.prgrms.ohouse.web.requests.UserUpdateRequest;
import com.prgrms.ohouse.web.results.UserInfoResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0")
public class UserRestController {

	private final UserService userService;
	private final JwtAuthenticationProvider authenticationProvider;

	@PostMapping("/signUp")
	public ResponseEntity<String> signUp(@RequestBody @Valid UserCreateRequest request, Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException();
		}
		userService.signUp(request.toCommand());
		return ResponseEntity.ok().body("User Create Success");
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request, Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException();
		}
		JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getEmail(), request.getPassword());
		Authentication auth = authenticationProvider.authenticate(authToken);
		JwtAuthentication jwtAuthentication = (JwtAuthentication)auth.getPrincipal();

		return ResponseEntity.ok()
			.header("Authorization", jwtAuthentication.token)
			.body("Login succeed.");
	}

	@GetMapping("/user")
	public ResponseEntity<UserInfoResult> getUserInfo() {
		User user = AuthUtils.getAuthUser();
		return ResponseEntity.ok()
			.body(UserInfoResult.build(user));
	}

	@PutMapping("/user")
	public ResponseEntity<UserInfoResult> modifyUserInfo(@RequestBody @Valid UserUpdateRequest request, Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException();
		}
		User updatedUser = userService.updateUser(AuthUtils.getAuthUser(), request.toCommand());
		return ResponseEntity.ok()
			.body(UserInfoResult.build(updatedUser));
	}
}