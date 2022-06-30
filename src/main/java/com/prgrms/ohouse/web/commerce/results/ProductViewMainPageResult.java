package com.prgrms.ohouse.web.commerce.results;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;

import lombok.Getter;

@Getter
public class ProductViewMainPageResult {
	private final String name;
	private final int price;
	private final Shipping shipping;
	private final String brand;

	public ProductViewMainPageResult(Product product) {
		this.name = product.getName();
		this.price = product.getPrice();
		this.shipping = product.getAttribute().getShipping();
		this.brand = product.getAttribute().getBrand();
	}
}
