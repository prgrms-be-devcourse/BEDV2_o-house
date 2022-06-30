package com.prgrms.ohouse.web.commerce.results;

import lombok.Getter;

@Getter
public class CartCreateResult {
	private int cartSize;

	public CartCreateResult(int cartSize) {
		this.cartSize = cartSize;
	}
}
