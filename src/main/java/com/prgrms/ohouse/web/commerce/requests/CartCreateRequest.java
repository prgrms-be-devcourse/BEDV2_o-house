package com.prgrms.ohouse.web.commerce.requests;

import lombok.Getter;

@Getter
public class CartCreateRequest {
	private Long productId;
	private int itemCount;

	public CartCreateRequest(Long productId, int itemCount) {
		this.itemCount = itemCount;
		this.productId = productId;
		setProductId(productId);
		setItemCount(itemCount);
	}

	private void setProductId(Long productId) {
		if (productId < 1) {
			throw new IllegalArgumentException("잘못된 프로덕트 아이디입니다.");
		}
		this.productId = productId;
	}

	private void setItemCount(int itemCount) {
		if (itemCount < 1) {
			throw new IllegalArgumentException("잘못된 카운트입니다.");
		}
		this.itemCount = itemCount;
	}
}
