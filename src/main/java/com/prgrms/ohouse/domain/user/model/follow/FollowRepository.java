package com.prgrms.ohouse.domain.user.model.follow;

import java.util.Optional;

import com.prgrms.ohouse.domain.user.model.User;

public interface FollowRepository {

	Follow save(Follow follow);

	Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);

	void delete(Follow follow);

	void deleteAll();
}
