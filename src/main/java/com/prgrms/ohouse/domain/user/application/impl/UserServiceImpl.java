package com.prgrms.ohouse.domain.user.application.impl;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserUpdateCommand;
import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.FailedLoginException;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserNotFoundException;
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
	private final FileManager fileManager;

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
			.orElseThrow(() -> new FailedLoginException("User not found. Login failed."));
		user.checkPassword(passwordEncoder, command.getPassword());
		return user;
	}

	@Transactional
	public User updateUser(User user, UserUpdateCommand command) {
		User updatedUser = userRepository.findByEmail(user.getEmail())
			.orElseThrow(() -> new UserNotFoundException("User not found. Try Again."));

		if (!command.getImage().isEmpty()) {
			StoredFile image = fileManager.store(command.getImage(), updatedUser);
			updatedUser.update(command.getNickname(), command.getGender(), command.getPersonalUrl(),
				command.getBirth(), image, command.getIntroductions());
		} else {
			updatedUser.update(command.getNickname(), command.getGender(), command.getPersonalUrl(),
				command.getBirth(), null, command.getIntroductions());
		}

		return updatedUser;
	}
}
