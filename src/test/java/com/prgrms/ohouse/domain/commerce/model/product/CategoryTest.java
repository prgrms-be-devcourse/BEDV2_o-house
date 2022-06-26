package com.prgrms.ohouse.domain.commerce.model.product;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.prgrms.ohouse.domain.commerce.model.product.Category;
import com.prgrms.ohouse.domain.commerce.model.product.enums.FourthCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.RootCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.SecondCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.ThirdCategory;

class CategoryTest {

	@Test
	void Category_of_정상_테스트() {
		//when
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME, FourthCategory.NORMAL);
		 //then
		assertThat(category).isNotNull();
	}
}