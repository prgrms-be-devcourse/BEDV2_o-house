package com.prgrms.ohouse.domain.user.application.impl;

import java.util.Optional;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserLoginCommand;
import com.prgrms.ohouse.domain.user.application.commands.UserUpdateCommand;
import com.prgrms.ohouse.domain.user.model.exception.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.exception.DuplicateNicknameException;
import com.prgrms.ohouse.domain.user.model.exception.FailedLoginException;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.exception.UserNotFoundException;
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

	@Transactional
	public void signUp(UserCreateCommand command) {

		if (userRepository.findByEmail(command.getEmail()).isPresent()) {
			throw new DuplicateEmailException("Duplicate Email. -> " + command.getEmail());
		}
		if (userRepository.findByNickname(command.getNickname()).isPresent()) {
			throw new DuplicateNicknameException("Duplicate Nickname. -> " + command.getNickname());
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
	public User updateUser(Long userId, UserUpdateCommand command) {
		User updatedUser = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("AuthUser not found. Try Again."));
		Optional<User> findUser = userRepository.findByNickname(command.getNickname());
		if (findUser.isPresent() && !updatedUser.equals(findUser.get())) {
			throw new DuplicateNicknameException("Duplicate Nickname. -> " + command.getNickname());
		}

		if (updatedUser.getImage().isPresent()) {
			fileManager.delete(updatedUser.getImage().get(), updatedUser);
		}
		if (!command.getImage().isEmpty()) {
			fileManager.store(command.getImage(), updatedUser);
		}
		updatedUser.update(command.getNickname(), command.getGender(), command.getPersonalUrl(),
			command.getBirth(), command.getIntroductions());
		return updatedUser;
	}
}
