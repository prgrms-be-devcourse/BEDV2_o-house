package com.prgrms.ohouse.domain.commerce.model.cart;

import java.util.Optional;

import com.prgrms.ohouse.domain.user.model.User;

public interface CartRepository {
	Optional<Cart> findByUser(User user);
}
