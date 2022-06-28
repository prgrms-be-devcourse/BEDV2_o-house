package com.prgrms.ohouse.domain.user.application.commands;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.user.model.GenderType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateCommand {
	@NotBlank(message = "닉네임 값은 필수로 입력해야 합니다.")
	private String nickname;

	private GenderType gender;

	@Pattern(regexp = "^((http(s?))\\:\\/\\/)([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$")
	private String personalUrl;

	private Date birth;

	private MultipartFile image;

	@Length(max = 255, message = "255자 이내의 소개글만 입력 가능합니다.")
	private String introductions;
}
