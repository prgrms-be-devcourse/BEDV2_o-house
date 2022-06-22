package com.prgrms.ohouse.web.requests;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

	@NotBlank(message = "이메일을 입력하세요.")
	private String email;

	@NotBlank(message = "비밀번호를 입력하세요.")
	private String password;

}
