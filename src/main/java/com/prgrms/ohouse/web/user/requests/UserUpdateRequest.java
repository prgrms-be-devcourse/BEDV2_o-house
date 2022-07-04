package com.prgrms.ohouse.web.user.requests;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prgrms.ohouse.domain.user.application.commands.UserUpdateCommand;
import com.prgrms.ohouse.domain.user.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

	@NotBlank(message = "닉네임 값은 필수로 입력해야 합니다.")
	private String nickname;

	private String gender;

	@Pattern(regexp = "^((http(s?))\\:\\/\\/)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$")
	private String personalUrl;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birth;

	@Length(max = 40, message = "40자 이내의 소개글만 입력 가능합니다.")
	private String introductions;

	public UserUpdateCommand toCommand(MultipartFile image) {
		return new UserUpdateCommand(nickname, Gender.of(gender), personalUrl, birth, image, introductions);
	}
}
