package com.prgrms.ohouse.web.commerce.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.web.ApiDocumentUtils;
import com.prgrms.ohouse.domain.commerce.application.CartService;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.web.commerce.requests.CartCreateRequest;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class RestCartControllerTest {
	@Mock
	private CartService cartService;

	@Autowired
	TestDataProvider fixtureProvider;

	@Autowired
	private MockMvc mockMvc;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void createCartItem() throws Exception {
		//given
		var userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NTYzMTU3NDQsImV4cCI6MTY1ODA0Mzc0NH0.SN55dE55PSha8BpAFP_J6zd113Tnnk2eDF1Ni2Gd53U";
		User user = fixtureProvider.insertGuestUser("guest");
		Product product = fixtureProvider.insertProduct();
		//when
		ResultActions result = this.mockMvc.perform(
			post("/api/v0/cart").
				header("Authorization", userToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new CartCreateRequest(product.getId(), 1)))
				.accept(MediaType.APPLICATION_JSON));
		//then
		result.
			andExpect(status().isOk())
			.andDo(MockMvcRestDocumentation.document("cart-create",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestFields(
					fieldWithPath("productId").type(JsonFieldType.NUMBER).description("장바구니에 담을 상품 넘버"),
					fieldWithPath("itemCount").type(JsonFieldType.NUMBER).description("상품 개수")
				),
				responseFields(
					fieldWithPath("cartSize").type(JsonFieldType.NUMBER).description("장바구니에 담긴 상품 개수")
				)
			));
	}
}