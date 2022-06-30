package com.prgrms.ohouse.domain.user.model.follow;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "follow")
public class Follow {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	private User fromUser;

	@ManyToOne
	private User toUser;

	public static Follow create(User fromUser, User toUser) {
		Follow follow = new Follow();
		follow.setFromUser(fromUser);
		follow.setToUser(toUser);
		return follow;
	}

	private void setFromUser(User fromUser) {
		this.fromUser = fromUser;
		fromUser.addFollowing();
	}

	private void setToUser(User toUser) {
		this.toUser = toUser;
		toUser.addFollower();
	}
}
