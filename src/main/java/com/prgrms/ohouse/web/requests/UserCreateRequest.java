package com.prgrms.ohouse.web.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
	@NotBlank(message = "닉네임을 비워둘 수 없습니다.")
	private String nickname;

	@NotBlank(message = "이메일을 비워둘 수 없습니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private String email;

	@NotBlank(message = "비밀번호를 비워둘 수 없습니다.")
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,20}",
		message = "비밀번호는 영문자와 숫자가 적어도 한 개 이상 포함된 8~20자이어야 합니다.")
	private String password;

	public UserCreateCommand toCommand() {
		return new UserCreateCommand(nickname, email, password);
	}
}
