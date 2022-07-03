package com.prgrms.ohouse.web.user.api;

import static org.springframework.http.MediaType.*;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.common.security.JwtAuthentication;
import com.prgrms.ohouse.domain.common.security.JwtAuthenticationProvider;
import com.prgrms.ohouse.domain.common.security.JwtAuthenticationToken;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.web.user.requests.UserCreateRequest;
import com.prgrms.ohouse.web.user.requests.UserLoginRequest;
import com.prgrms.ohouse.web.user.requests.UserUpdateRequest;
import com.prgrms.ohouse.web.user.results.UserInfoResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0")
public class UserRestController {

	private final UserService userService;
	private final JwtAuthenticationProvider authenticationProvider;
	private final AuthUtility authUtility;

	@PostMapping("/signup")
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
	public ResponseEntity<UserInfoResult> getUserInformation() {
		User user = authUtility.getAuthUser();
		return ResponseEntity.ok()
			.body(UserInfoResult.build(user));
	}

	@PostMapping(value = "/user",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<String> modifyUserInformation(@RequestPart(name = "image", required = false) MultipartFile file,
		@Valid @RequestPart("request") UserUpdateRequest request, Errors errors) {
		if (errors.hasErrors()) {
			throw new IllegalArgumentException();
		}
		userService.updateUser(authUtility.getAuthUser().getId(), request.toCommand(file));
		return ResponseEntity.ok()
			.body("Update succeed.");
	}
}
