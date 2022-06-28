package com.prgrms.ohouse.web.user.requests;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prgrms.ohouse.domain.user.application.commands.UserUpdateCommand;
import com.prgrms.ohouse.domain.user.model.GenderType;

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

	//TODO formatting 확인. null 입력시 확인
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date birth;

	@Length(max = 255, message = "255자 이내의 소개글만 입력 가능합니다.")
	private String introductions;

	public UserUpdateCommand toCommand(MultipartFile image) {
		GenderType type = GenderType.of(gender);
		return new UserUpdateCommand(nickname, type, personalUrl, birth, image, introductions);
	}
}
