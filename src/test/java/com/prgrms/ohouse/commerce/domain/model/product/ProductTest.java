package com.prgrms.ohouse.commerce.domain.model.product;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.prgrms.ohouse.commerce.enums.Color;
import com.prgrms.ohouse.commerce.enums.FourthCategory;
import com.prgrms.ohouse.commerce.enums.RootCategory;
import com.prgrms.ohouse.commerce.enums.SecondCategory;
import com.prgrms.ohouse.commerce.enums.Shipping;
import com.prgrms.ohouse.commerce.enums.Size;
import com.prgrms.ohouse.commerce.enums.ThirdCategory;

class ProductTest {

	@Test
	void Product_of_정상_테스트() {
		//given
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		//when
		Product product = Product.of("이름입니다.", 234, "내용입니다.", category, attribute);
		//then
		assertThat(product).isNotNull();
	}

	@Test
	void Product_of_실패_테스트() {
		//given
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		//when //then
		Assertions.assertThatThrownBy(() -> Product.of("이름입니다.", 0, "내용입니다.", category, attribute))
			.isInstanceOf(IllegalArgumentException.class);
		Assertions.assertThatThrownBy(() -> Product.of("이름입니다.", 10000000, "내용입니다.", category, attribute))
			.isInstanceOf(IllegalArgumentException.class);
	}
}