package com.prgrms.ohouse.web.commerce.results;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;

@Getter
public class OrderViewItemResult {
	private Long orderId;
	private LocalDateTime createdAt;
	private List<OrderViewProductResult> orderViewOrderItemResults;

	public OrderViewItemResult(Long orderId, LocalDateTime createdAt,
		List<OrderViewProductResult> orderViewOrderItemResults) {
		this.orderId = orderId;
		this.createdAt = createdAt;
		this.orderViewOrderItemResults = orderViewOrderItemResults;
	}

}
