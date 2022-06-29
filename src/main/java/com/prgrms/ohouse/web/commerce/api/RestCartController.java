package com.prgrms.ohouse.web.commerce.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.commerce.application.CartService;
import com.prgrms.ohouse.domain.commerce.application.command.CartCreateCommand;
import com.prgrms.ohouse.domain.common.security.AuthUtils;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.web.commerce.results.CartCreateResult;
import com.prgrms.ohouse.web.commerce.requests.CartCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestCartController {
	private final CartService cartService;

	@PostMapping("/api/v0/cart")
	public ResponseEntity<CartCreateResult> createCartItem(@RequestBody CartCreateRequest cartCreateRequest) {
		User user = AuthUtils.getAuthUser();
		return ResponseEntity.ok()
			.body(cartService.insertCartItem(
				new CartCreateCommand(cartCreateRequest.getProductId(), cartCreateRequest.getItemCount(), user)));
	}
}
