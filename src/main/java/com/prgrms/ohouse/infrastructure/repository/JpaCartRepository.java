package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.cart.Cart;
import com.prgrms.ohouse.domain.commerce.model.cart.CartRepository;

public interface JpaCartRepository extends JpaRepository<Cart, Long>, CartRepository {
}
