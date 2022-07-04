package com.prgrms.ohouse.domain.commerce.model.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
import com.prgrms.ohouse.domain.user.model.Address;
import com.prgrms.ohouse.domain.user.model.User;

class OrderTest {

	@Test
	void Order_생성_정상_테스트() {
		//given
		//when
		Order order = Order.of(new Address());
		//then
		assertThat(order.getTotalPrice()).isZero();
	}

	@Test
	void Order_totalPrice_정상_테스트() {
		//given
		User user = User.builder()
			.email("test@email.com")
			.nickname("nickname")
			.password("password")
			.followerCount(0)
			.defaultAddress(new Address())
			.build();
		Order order = Order.of(new Address());
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		Product product = Product.of("이름입니다.", 234, "내용입니다.", category, attribute);
		//when
		OrderItem orderItem1 = OrderItem.builder().order(order).price(123).product(product)
			.quantity(3).build();
		OrderItem orderItem2 = OrderItem.builder().order(order).price(321).product(product)
			.quantity(2).build();
		//then
		assertThat(order.getOrderItems()).hasSize(2);
	}
}