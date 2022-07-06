package com.prgrms.ohouse.web.commerce.results;

import com.prgrms.ohouse.domain.commerce.model.order.DeliveryType;

import lombok.Getter;

@Getter
public class OrderViewProductResult {
	private String productName;
	private int productPrice;
	private int quantity;
	DeliveryType deliveryType;

	public OrderViewProductResult(String productName, int productPrice, int quantity, DeliveryType deliveryType) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.quantity = quantity;
		this.deliveryType = deliveryType;
	}
}
