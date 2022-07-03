package com.prgrms.ohouse.domain.commerce.application.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.CartService;
import com.prgrms.ohouse.domain.commerce.application.command.CartCreateCommand;
import com.prgrms.ohouse.domain.commerce.model.cart.Cart;
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
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.web.commerce.results.CartCreateResult;

@Transactional
@SpringBootTest
class CartServiceImplTest {
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AttributeRepository attributeRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void insertCartItem_정상_테스트() throws Exception {
		//given
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		categoryRepository.save(category);
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		attributeRepository.save(attribute);
		Product product = Product.of("이름입니다.", 234, "내용입니다.", category, attribute);

		User user = User.builder()
			.nickname("guestUser")
			.email("guest@gmail.com")
			.password("testPassword12")
			.build();
		userRepository.save(user);
		Cart cart = Cart.of(user);
		productRepository.save(product);
		//when
		CartCreateResult selectedCartItem = cartService.insertCartItem(new CartCreateCommand(product.getId(), 1, user));
		//then
		assertThat(selectedCartItem.getCartSize()).isEqualTo(1);
	}
}