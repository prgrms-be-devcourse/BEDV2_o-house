package com.prgrms.ohouse.domain.commerce.model.product.enums;

import java.util.function.Function;

public enum PriceRange {
	ZERO(price -> price > 0), MAX(price -> price < 10000000);

	private final Function<Integer, Boolean> valid;

	private PriceRange(Function<Integer, Boolean> validRange) {
		this.valid = validRange;
	}

	public boolean validRange(int price) {
		return valid.apply(price);
	}
}
