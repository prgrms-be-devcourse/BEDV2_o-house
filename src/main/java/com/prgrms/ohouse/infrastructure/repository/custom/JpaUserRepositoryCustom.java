package com.prgrms.ohouse.infrastructure.repository.custom;

import com.prgrms.ohouse.domain.user.model.User;

public interface JpaUserRepositoryCustom {

	void addFollowingCount(User user);

	void addFollowerCount(User user);

	void subFollowingCount(User user);

	void subFollowerCount(User user);

}
