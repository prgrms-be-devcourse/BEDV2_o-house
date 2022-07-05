package com.prgrms.ohouse.web.user.results;

import java.time.LocalDate;

import com.prgrms.ohouse.domain.user.model.UserImage;
import com.prgrms.ohouse.domain.user.model.Gender;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.Data;

@Data
public class UserInfoResult {

	private String nickname;

	private Gender gender;

	private String personalUrl;

	private LocalDate birth;

	private UserImage image;

	private String introductions;

	public static UserInfoResult build(User user) {
		return new UserInfoResult(user.getNickname(), user.getGender(), user.getPersonalUrl(), user.getBirth(),
			user.getImage().isPresent() ? user.getImage().get() : null, user.getIntroductions());
	}

	private UserInfoResult(String nickname, Gender gender, String personalUrl, LocalDate birth, UserImage image,
		String introductions) {
		this.nickname = nickname;
		this.gender = gender;
		this.personalUrl = personalUrl;
		this.birth = birth;
		this.image = image;
		this.introductions = introductions;
	}
}
