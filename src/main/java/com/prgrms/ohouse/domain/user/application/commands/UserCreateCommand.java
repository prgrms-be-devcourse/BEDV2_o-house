package com.prgrms.ohouse.domain.user.application.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class UserCreateCommand {

	@NotBlank
	private final String nickname;
	@NotBlank
	private final String email;
	@NotBlank
	private final String password;

}
