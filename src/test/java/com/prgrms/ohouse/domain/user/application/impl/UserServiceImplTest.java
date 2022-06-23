package com.prgrms.ohouse.domain.user.application.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@SpringBootTest
class UserServiceImplTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private UserServiceImpl userService;
	private UserCreateCommand createCommand = new UserCreateCommand("testname", "test@gmail.com", "testPassword");

	@BeforeEach
	void init() {
		userService = new UserServiceImpl(userRepository, passwordEncoder);
	}

	@Test
	@Transactional
	void signUpTest() throws DuplicateEmailException {
		userService.signUp(createCommand);
		User findUser = userRepository.findByEmail(createCommand.getEmail()).get();
		assertThat(passwordEncoder.matches(createCommand.getPassword(), findUser.getPassword()), is(true));
	}

	@Test
	@Transactional
	void loginTest() throws DuplicateEmailException {
		userService.signUp(createCommand);
		UserLoginCommand command = new UserLoginCommand(createCommand.getEmail(), createCommand.getPassword());

		User loginUser = userService.login(command);

		User findUser = userRepository.findByEmail(command.getEmail()).get();
		assertThat(loginUser, is(samePropertyValuesAs(findUser)));
	}
}