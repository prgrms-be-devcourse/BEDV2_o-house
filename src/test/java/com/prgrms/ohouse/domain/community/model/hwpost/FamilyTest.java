package com.prgrms.ohouse.domain.community.model.hwpost;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.prgrms.ohouse.domain.community.model.housewarming.Family;

class FamilyTest {

	@ParameterizedTest
	@DisplayName("예약된 문자열을 enum으로 바꾸는가")
	@CsvSource({"SINGLE", "MARRIED", "CHILDREN", "PARENTS", "ROOMMATES"})
	void map_to_correct_type(String typeString) {

		// When
		assertThatNoException()
			.isThrownBy(() -> new Family(typeString, null, null));

	}

}
