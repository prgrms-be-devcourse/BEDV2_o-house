package com.prgrms.ohouse.domain.user.application.impl;

import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void signUp(UserCreateCommand command) {

		if (userRepository.findByEmail(command.getEmail()).isPresent()) {
			throw new DuplicateEmailException("Duplicate Email. -> " + command.getEmail());
		}

		User newUser = User.builder()
			.nickname(command.getNickname())
			.email(command.getEmail())
			.password(passwordEncoder.encode(command.getPassword()))
			.build();
		userRepository.save(newUser);
	}

	@Transactional(readOnly = true)
	public User login(UserLoginCommand command) {
		User user = userRepository.findByEmail(command.getEmail())
			.orElseThrow(() -> new IllegalArgumentException("User not found. Login failed."));
		user.checkPassword(passwordEncoder, command.getPassword());
		return user;
	}
}
