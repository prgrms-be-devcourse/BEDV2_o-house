package com.prgrms.ohouse.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.user.model.Address;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@Component
@Transactional
public class TestDataProvider {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AttributeRepository attributeRepository;

	public User insertUser() {
		User user = User.builder()
			.email("test@email.com")
			.nickname("nickname")
			.password("password")
			.followerCount(0)
			.defaultAddress(new Address())
			.build();
		return userRepository.save(user);
	}

	public Category insertCategory() {
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		return categoryRepository.save(category);
	}

	public Attribute insertAttribute() {
		return attributeRepository.save(Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL));

	}

	public Product insertProduct() {
		Category category = insertCategory();
		Attribute attribute = insertAttribute();
		Product product = Product.of("product", 2000, "content", category, attribute);
		return productRepository.save(product);
	}
}
