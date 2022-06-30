package com.prgrms.ohouse.domain.user.model.follow;

import javax.persistence.PreRemove;

public class UnFollowListener {

	@PreRemove
	public void unfollow(Follow follow) {
		follow.getFromUser().subFollowing();
		follow.getToUser().subFollower();
	}
}