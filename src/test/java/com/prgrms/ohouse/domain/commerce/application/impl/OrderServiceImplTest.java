package com.prgrms.ohouse.domain.commerce.application.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.OrderService;
import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.domain.commerce.application.command.OrderItemAddCommand;
import com.prgrms.ohouse.domain.commerce.model.order.OrderRepository;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.web.commerce.requests.OrderAddRequest;
import com.prgrms.ohouse.web.commerce.requests.OrderItemAddRequest;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;

@Transactional
@SpringBootTest
class OrderServiceImplTest {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	TestDataProvider testDataProvider;
	@MockBean
	UserAuditorAware userAuditorAware;
	@Test
	void addOrder_정상_테스트() {
		// given
		User user = testDataProvider.insertUser();
		when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
		List<OrderItemAddRequest> orderItemAddRequests = new ArrayList<>();
		Product product1 = testDataProvider.insertProduct();
		Product product2 = testDataProvider.insertProduct();
		orderItemAddRequests.add(new OrderItemAddRequest(product1.getId(), 1));
		orderItemAddRequests.add(new OrderItemAddRequest(product2.getId(), 2));
		List<OrderItemAddCommand> orderItemAddCommands = new ArrayList<>();
		orderItemAddRequests.forEach(orderItemAddRequest -> orderItemAddCommands.add(
			new OrderItemAddCommand(orderItemAddRequest.getProductId(), orderItemAddRequest.getQuantity())));
		OrderAddCommand orderAddCommand = new OrderAddCommand(OrderAddRequest.builder()
			.orderItemAddRequests(orderItemAddRequests).city("city").detail("detail")
			.email("email").shipper("shipper").street("street").zipcode("zipcode")
			.lotNumberAddress("lotNumberAddress").recipient("recipient")
			.shippingAddress("shippingAddress").build()
		);
		//when
		OrderAddResult orderAddResult = orderService.addOrder(orderAddCommand);
		//then
		assertThat(orderRepository.findById(orderAddResult.getOrderId())).isNotEmpty();
		assertThat(orderRepository.findById(orderAddResult.getOrderId()).get().getUser()).isNotNull();
	}
}