package com.prgrms.ohouse.domain.user.model;

import java.util.Optional;

public interface UserRepository {
	Optional<User> findByEmail(String email);

	Optional<User> findById(Long id);

	User save(User user);
}
