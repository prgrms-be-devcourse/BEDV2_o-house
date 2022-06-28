package com.prgrms.ohouse.web.user.results;

import java.util.Date;

import com.prgrms.ohouse.domain.common.file.UserImage;
import com.prgrms.ohouse.domain.user.model.GenderType;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.Data;

@Data
public class UserInfoResult {

	private String nickname;

	private GenderType gender;

	private String personalUrl;

	private Date birth;

	private UserImage image;

	private String introductions;

	public static UserInfoResult build(User user) {
		return new UserInfoResult(user.getNickname(), user.getGender(), user.getPersonalUrl(), user.getBirth(),
			user.getImage(), user.getIntroductions());
	}

	private UserInfoResult(String nickname, GenderType gender, String personalUrl, Date birth, UserImage image,
		String introductions) {
		this.nickname = nickname;
		this.gender = gender;
		this.personalUrl = personalUrl;
		this.birth = birth;
		this.image = image;
		this.introductions = introductions;
	}
}
