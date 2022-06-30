package com.prgrms.ohouse.domain.commerce.application.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.ProductService;
import com.prgrms.ohouse.web.commerce.results.ProductViewMainPageResult;
import com.prgrms.ohouse.web.commerce.results.SliceResult;
import com.prgrms.ohouse.domain.commerce.model.product.Attribute;
import com.prgrms.ohouse.domain.commerce.model.product.AttributeRepository;
import com.prgrms.ohouse.domain.commerce.model.product.Category;
import com.prgrms.ohouse.domain.commerce.model.product.CategoryRepository;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Color;
import com.prgrms.ohouse.domain.commerce.model.product.enums.FourthCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.RootCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.SecondCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Size;
import com.prgrms.ohouse.domain.commerce.model.product.enums.ThirdCategory;

@SpringBootTest
@Transactional
class ProductServiceImplTest {
	@Autowired
	ProductService productService;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	AttributeRepository attributeRepository;

	@BeforeEach
	void 자료_넣기() {
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		categoryRepository.save(category);
		attributeRepository.save(attribute);
		for (int i = 0; i < 50; i++) {
			productRepository.save(Product.of(Integer.toString(i), 123, "asd", category, attribute));
		}
	}

	@Test
	void findMainPageOrderByCreatedAtDesc_정상_테스트() {
		//given
		Pageable pageable = PageRequest.of(0, 5);
		//when
		SliceResult<ProductViewMainPageResult> mainPageOrderByCreatedAtDesc = productService.findMainPageOrderByCreatedAtDesc(
			pageable, "attribute");
		System.out.println(mainPageOrderByCreatedAtDesc.isHasNext());
		//then
		assertThat(mainPageOrderByCreatedAtDesc.getSize()).isEqualTo(5);
	}
}