package com.prgrms.ohouse.domain.commerce.application.converter;

import java.util.ArrayList;
import java.util.List;

import com.prgrms.ohouse.domain.commerce.model.order.Order;
import com.prgrms.ohouse.web.commerce.results.OrderViewItemResult;
import com.prgrms.ohouse.web.commerce.results.OrderViewProductResult;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderConverter {
	public static List<OrderViewItemResult> convertOrderToViewItem(List<Order> orders) {
		List<OrderViewItemResult> orderViewItemResults = new ArrayList<>();
		List<OrderViewProductResult> orderViewProductResults = new ArrayList<>();
		orders.forEach(order -> {
				orderViewItemResults.add(
					new OrderViewItemResult(order.getId(), order.getCreatedAt(), orderViewProductResults));
				order.getOrderItems().forEach(orderItem -> orderViewProductResults
					.add(new OrderViewProductResult(orderItem.getProduct().getName(), orderItem.getPrice(),
						orderItem.getQuantity(), orderItem.getDeliveryType())));
			}
		);
		return orderViewItemResults;
	}
}
