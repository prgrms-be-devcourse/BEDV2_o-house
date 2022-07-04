package com.prgrms.ohouse.domain.user.model;

import java.util.Optional;

public interface UserRepository {

	void addFollowingCount(User user);

	void addFollowerCount(User user);

	void subFollowingCount(User user);

	void subFollowerCount(User user);

	Optional<User> findByEmail(String email);

	Optional<User> findByNickname(String nickname);

	Optional<User> findById(Long id);

	User save(User user);

	void deleteAll();
}
