package com.prgrms.ohouse.web.commerce.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemAddRequest {
	private Long productId;
	private int quantity;

	public OrderItemAddRequest(Long productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}
}
