package com.prgrms.ohouse.domain.commerce.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.CartService;
import com.prgrms.ohouse.domain.commerce.application.command.CartCreateCommand;
import com.prgrms.ohouse.domain.commerce.model.cart.Cart;
import com.prgrms.ohouse.domain.commerce.model.cart.CartItem;
import com.prgrms.ohouse.domain.commerce.model.cart.CartItemRepository;
import com.prgrms.ohouse.domain.commerce.model.cart.CartRepository;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.web.commerce.results.CartCreateResult;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;

	@Override
	public CartCreateResult insertCartItem(CartCreateCommand cartCreateCommand) {
		Cart cart = cartRepository.findByUser(cartCreateCommand.getUser()).orElseThrow();
		CartItem cartItem = CartItem.of(cart,
			productRepository.findById(cartCreateCommand.getProductId()).orElseThrow());
		// cartItemRepository.save(cartItem);
		return new CartCreateResult(cart.getCartItems().size());
	}
}
