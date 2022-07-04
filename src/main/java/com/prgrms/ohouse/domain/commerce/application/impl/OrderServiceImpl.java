package com.prgrms.ohouse.domain.commerce.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.OrderService;
import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.domain.commerce.model.order.Order;
import com.prgrms.ohouse.domain.commerce.model.order.OrderItem;
import com.prgrms.ohouse.domain.commerce.model.order.OrderItemRepository;
import com.prgrms.ohouse.domain.commerce.model.order.OrderRepository;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.user.model.Address;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	@Override
	public OrderAddResult addOrder(OrderAddCommand orderAddCommand) {
		Order order = Order.of(new Address());
		orderRepository.save(order);
		orderAddCommand.getOrderItemCommands().forEach(orderItemAddCommand -> {
			Product product = productRepository.findById(orderItemAddCommand.getProductId()).orElseThrow();
			orderItemRepository.save(OrderItem.builder()
				.product(product)
				.price(product.getPrice())
				.quantity(orderItemAddCommand.getQuantity())
				.order(order)
				.build());
		});
		return new OrderAddResult(orderRepository.save(order).getId());
	}
}