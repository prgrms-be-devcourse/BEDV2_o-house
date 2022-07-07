package com.prgrms.ohouse.domain.commerce.model.order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
	Order save(Order order);

	Optional<Order> findById(Long id);

	List<Order> findByUserId(Long id);
}
