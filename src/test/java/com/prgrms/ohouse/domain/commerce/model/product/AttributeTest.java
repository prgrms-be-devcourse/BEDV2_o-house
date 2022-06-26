package com.prgrms.ohouse.domain.commerce.model.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.prgrms.ohouse.domain.commerce.model.product.Attribute;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Color;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Size;

class AttributeTest {

	@Test
	void Attribute_of_정상_테스트() {
		//when
		Attribute attribute = Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL);
		//then
		Assertions.assertThat(attribute).isNotNull();
	}
}