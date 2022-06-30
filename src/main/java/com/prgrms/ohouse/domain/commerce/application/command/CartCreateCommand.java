package com.prgrms.ohouse.domain.commerce.application.command;

import com.prgrms.ohouse.domain.user.model.User;

import lombok.Getter;

@Getter
public class CartCreateCommand {
	private Long productId;
	private int itemCount;
	private User user;

	public CartCreateCommand(Long productId, int itemCount, User user) {
		this.productId = productId;
		this.itemCount = itemCount;
		this.user = user;
	}
}
