package com.prgrms.ohouse.domain.user.application.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserUpdateCommand;
import com.prgrms.ohouse.domain.user.model.Gender;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@SpringBootTest(properties = "spring.profiles.active:test")
class UserServiceImplTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private FileManager fileManager;

	private UserServiceImpl userService;
	private UserCreateCommand createCommand = new UserCreateCommand("guest", "guest@gmail.com", "guestPw12");

	@BeforeEach
	void init() {
		userService = new UserServiceImpl(userRepository, passwordEncoder, fileManager);
		userService.signUp(createCommand);
	}

	@Test
	@Transactional
	void signUpTest() {
		User findUser = userRepository.findByEmail(createCommand.getEmail()).get();
		assertThat(passwordEncoder.matches(createCommand.getPassword(), findUser.getPassword()), is(true));
	}

	@Test
	@Transactional
	void loginTest(){
		UserLoginCommand command = new UserLoginCommand(createCommand.getEmail(), createCommand.getPassword());

		User loginUser = userService.login(command);

		User findUser = userRepository.findByEmail(command.getEmail()).get();
		assertThat(loginUser, is(samePropertyValuesAs(findUser)));
	}

	@Test
	@Transactional
	@DisplayName("유저 정보 수정 테스트")
	void updateUserTest(){

	    //given
		MockMultipartFile file = new MockMultipartFile(
			"mockingTestimage",
			"test.png",
			"image/png",
			"<<png data>>".getBytes()
		);

		UserLoginCommand loginCommand = new UserLoginCommand(createCommand.getEmail(), createCommand.getPassword());
		UserUpdateCommand command = new UserUpdateCommand(createCommand.getNickname(), Gender.FEMALE,
			"http://naver.com", LocalDate.now(), file, null);
		User user = userService.login(loginCommand);

	    //when
		User updatedUser = userService.updateUser(user.getId(), command);

	    //then
		User findUser = userRepository.findByEmail(loginCommand.getEmail()).get();
		assertThat(findUser, is(samePropertyValuesAs(updatedUser)));

	}
}