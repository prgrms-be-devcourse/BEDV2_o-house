package com.prgrms.ohouse.domain.commerce.application.command;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.ohouse.domain.user.model.Address;
import com.prgrms.ohouse.web.commerce.requests.OrderAddRequest;

import lombok.Getter;

@Getter
public class OrderAddCommand {
	private List<OrderItemAddCommand> orderItemCommands = new ArrayList<>();
	private Address address;

	public OrderAddCommand(
		OrderAddRequest orderAddRequest) {
		orderAddRequest.getOrderItemAddRequests().forEach(orderItemAddRequest ->
			orderItemCommands.add(new OrderItemAddCommand(orderItemAddRequest.getProductId(),
				orderItemAddRequest.getQuantity())));
		this.address = new Address();
	}
}
