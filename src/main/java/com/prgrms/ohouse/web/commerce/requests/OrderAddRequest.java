package com.prgrms.ohouse.web.commerce.requests;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAddRequest {
	private String shippingAddress;
	private String recipient;
	private String city;
	private String street;
	private String detail;
	private String zipcode;
	private String lotNumberAddress;
	private String shipper;
	private String email;
	private List<OrderItemAddRequest> orderItemAddRequests;

	@Builder
	public OrderAddRequest(String shippingAddress, String recipient, String city, String street, String detail,
		String zipcode, String lotNumberAddress, String shipper, String email,
		List<OrderItemAddRequest> orderItemAddRequests) {
		this.shippingAddress = shippingAddress;
		this.recipient = recipient;
		this.city = city;
		this.street = street;
		this.detail = detail;
		this.zipcode = zipcode;
		this.lotNumberAddress = lotNumberAddress;
		this.shipper = shipper;
		this.email = email;
		this.orderItemAddRequests = orderItemAddRequests;
	}
}
