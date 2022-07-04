package com.prgrms.ohouse.web.commerce.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.commerce.application.OrderService;
import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.web.ApiDocumentUtils;
import com.prgrms.ohouse.web.commerce.requests.OrderAddRequest;
import com.prgrms.ohouse.web.commerce.requests.OrderItemAddRequest;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class RestOrderControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	TestDataProvider fixtureProvider;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OrderService orderService;

	@Test
	void addNewOrder_정상_테스트() throws Exception {
		//given
		var userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NTYzMTU3NDQsImV4cCI6MTY1ODA0Mzc0NH0.SN55dE55PSha8BpAFP_J6zd113Tnnk2eDF1Ni2Gd53U";
		User user = fixtureProvider.insertGuestUser("guest");
		OrderAddResult orderAddResult = new OrderAddResult(1L);
		given(orderService.addOrder(any(OrderAddCommand.class))).willReturn(orderAddResult);
		//when
		List<OrderItemAddRequest> orderItemAddRequests = new ArrayList<>();
		orderItemAddRequests.add(new OrderItemAddRequest(2L, 1));
		orderItemAddRequests.add(new OrderItemAddRequest(3L, 2));
		String orderRequestJson = objectMapper.writeValueAsString(OrderAddRequest.builder()
			.orderItemAddRequests(orderItemAddRequests).city("city").detail("detail")
			.email("email").shipper("shipper").street("street").zipcode("zipcode")
			.lotNumberAddress("lotNumberAddress").recipient("recipient")
			.shippingAddress("shippingAddress").build());
		ResultActions result = this.mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v0/order")
				.header("Authorization", userToken)
				.content(orderRequestJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		//then
		result.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcRestDocumentation.document("order-add",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				PayloadDocumentation.requestFields(
					PayloadDocumentation.fieldWithPath("shipper").type(JsonFieldType.STRING).description("배송인 명"),
					PayloadDocumentation.fieldWithPath("shippingAddress").type(JsonFieldType.STRING).description("배송지 명"),
					PayloadDocumentation.fieldWithPath("recipient").type(JsonFieldType.STRING).description("수령인"),
					PayloadDocumentation.fieldWithPath("city").type(JsonFieldType.STRING).description("배송지 도시"),
					PayloadDocumentation.fieldWithPath("street").type(JsonFieldType.STRING).description("배송지 지번"),
					PayloadDocumentation.fieldWithPath("detail").type(JsonFieldType.STRING).description("상세 주소"),
					PayloadDocumentation.fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호"),
					PayloadDocumentation.fieldWithPath("lotNumberAddress").type(JsonFieldType.STRING).description("지번"),
					PayloadDocumentation.fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					PayloadDocumentation.fieldWithPath("orderItemAddRequests[].productId").type(JsonFieldType.NUMBER).description("주문 상품 상품 아이디"),
					PayloadDocumentation.fieldWithPath("orderItemAddRequests[].quantity").type(JsonFieldType.NUMBER).description("주문 상품 상품 개수")
					),
				PayloadDocumentation.responseFields(
					PayloadDocumentation.fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("등록 오더 아이디")
				)
			));

	}
}