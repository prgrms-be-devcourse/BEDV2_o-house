package com.prgrms.ohouse.domain.common.file;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends StoredFile{

	public UserImage(String originalFileName, String url, User user) {
		super(originalFileName, url);
		this.user = user;
	}

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}
