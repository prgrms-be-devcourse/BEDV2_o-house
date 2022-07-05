package com.prgrms.ohouse.domain.commerce.application.command;

import lombok.Getter;

@Getter
public class OrderItemAddCommand {
	private Long productId;
	private int quantity;

	public OrderItemAddCommand(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
}
