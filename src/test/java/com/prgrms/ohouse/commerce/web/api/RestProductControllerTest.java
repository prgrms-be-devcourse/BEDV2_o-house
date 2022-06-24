package com.prgrms.ohouse.commerce.web.api;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.commerce.domain.application.ProductService;
import com.prgrms.ohouse.commerce.domain.application.command.ProductViewMainPageCommand;
import com.prgrms.ohouse.commerce.domain.application.command.SliceCommand;
import com.prgrms.ohouse.commerce.domain.model.product.Attribute;
import com.prgrms.ohouse.commerce.domain.model.product.Category;
import com.prgrms.ohouse.commerce.domain.model.product.Product;
import com.prgrms.ohouse.commerce.enums.Color;
import com.prgrms.ohouse.commerce.enums.FourthCategory;
import com.prgrms.ohouse.commerce.enums.RootCategory;
import com.prgrms.ohouse.commerce.enums.SecondCategory;
import com.prgrms.ohouse.commerce.enums.Shipping;
import com.prgrms.ohouse.commerce.enums.Size;
import com.prgrms.ohouse.commerce.enums.ThirdCategory;

@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(RestProductController.class)
@AutoConfigureRestDocs
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
		List<ProductViewMainPageCommand> arrays = new ArrayList<>();
		Product product = Product.of("이름입니다.", 234, "내용입니다.", category, attribute);
		arrays.add(new ProductViewMainPageCommand(product));
		given(productService.findMainPageOrderByCreatedAtDesc(pageable, "asd")).willReturn(
			new SliceCommand<ProductViewMainPageCommand>(
				new SliceImpl<ProductViewMainPageCommand>(arrays, pageable, false)));
		//when
		this.mockMvc.perform(get("/api/v0/products").param("attribute", "asd").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("product-view",
				requestParameters(
					parameterWithName("attribute").description("속성")
				),
				responseFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("물건 내용"),
					fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
					fieldWithPath("isLastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지인지 확인"),
					fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지가 있는지 확인")
				)
			));
	}
}