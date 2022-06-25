package com.prgrms.ohouse.domain.user.application.commands;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserLoginCommand {

	@NotBlank
	private final String email;
	@NotBlank
	private final String password;
}
