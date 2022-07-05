package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.order.Order;
import com.prgrms.ohouse.domain.commerce.model.order.OrderRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {
}
