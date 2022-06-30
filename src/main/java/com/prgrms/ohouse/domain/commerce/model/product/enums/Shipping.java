package com.prgrms.ohouse.domain.commerce.model.product.enums;

public enum Shipping {
	FREE(0), NORMAL(3000);
	private int shippingPrice;

	private Shipping(int shippingPrice) {
		this.shippingPrice = shippingPrice;
	}

	public int getShippingPrice() {
		return shippingPrice;
	}
}
