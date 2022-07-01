package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.cart.CartItem;
import com.prgrms.ohouse.domain.commerce.model.cart.CartItemRepository;

public interface JpaCartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepository {
}
