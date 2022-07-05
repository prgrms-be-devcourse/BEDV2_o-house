package com.prgrms.ohouse.web.commerce.results;

import lombok.Getter;

@Getter
public class OrderAddResult {
	Long orderId;

	public OrderAddResult(Long orderId) {
		this.orderId = orderId;
	}
}
