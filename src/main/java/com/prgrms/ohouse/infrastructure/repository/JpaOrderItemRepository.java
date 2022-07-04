package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.order.OrderItem;
import com.prgrms.ohouse.domain.commerce.model.order.OrderItemRepository;

public interface JpaOrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepository {
}
