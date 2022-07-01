package com.prgrms.ohouse.web.user.requests;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.prgrms.ohouse.web.user.requests.UserCreateRequest;

class UserCreateRequestTest {

	private static ValidatorFactory factory;
	private static Validator validator;

	@BeforeAll
	static void setup() {
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void emailValidationFailedTest() {
		UserCreateRequest request = new UserCreateRequest("user", "awwwfdojf.com", "testPassword12");

		Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
		assertThat(violations, hasSize(1));
	}

	@Test
	void passwordValidationFailedTest() {
		UserCreateRequest request = new UserCreateRequest("user", "test@gmail.com", "pppasss");
		Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(request);
		assertThat(violations, hasSize(1));
	}

}