package com.prgrms.ohouse.web.commerce.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.commerce.application.ProductService;
import com.prgrms.ohouse.domain.commerce.model.product.Attribute;
import com.prgrms.ohouse.domain.commerce.model.product.Category;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Color;
import com.prgrms.ohouse.domain.commerce.model.product.enums.FourthCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.RootCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.SecondCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Size;
import com.prgrms.ohouse.domain.commerce.model.product.enums.ThirdCategory;
import com.prgrms.ohouse.web.ApiDocumentUtils;
import com.prgrms.ohouse.web.commerce.results.ProductViewMainPageResult;
import com.prgrms.ohouse.web.commerce.results.SliceResult;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class RestProductControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	@Test
	void viewProductsByAttribute() throws Exception {
		//given
		Pageable pageable = PageRequest.of(0, 20);
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		List<ProductViewMainPageResult> arrays = new ArrayList<>();
		Product product = Product.of("???????????????.", 234, "???????????????.", category, attribute);
		arrays.add(new ProductViewMainPageResult(product));
		given(productService.findMainPageOrderByCreatedAtDesc(pageable, "asd")).willReturn(
			new SliceResult<ProductViewMainPageResult>(
				new SliceImpl<ProductViewMainPageResult>(arrays, pageable, false)));
		//when
		ResultActions result = this.mockMvc.perform(
			get("/api/v0/products").param("attribute", "asd").accept(MediaType.APPLICATION_JSON));
		//then
		result.andExpect(status().isOk())
			.andDo(MockMvcRestDocumentation.document("product-view",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestParameters(
					parameterWithName("attribute").description("??????")
				),
				responseFields(
					fieldWithPath("contents[].name").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("contents[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("contents[].shipping").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("contents[].brand").type(JsonFieldType.STRING).description("????????? ??????"),
					fieldWithPath("size").type(JsonFieldType.NUMBER).description("????????? ?????????"),
					fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("?????? ???????????? ????????? ??????"),
					fieldWithPath("lastPage").type(JsonFieldType.BOOLEAN).description("????????? ??????????????? ??????")
				)
			));
	}
}