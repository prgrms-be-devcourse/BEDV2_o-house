package com.prgrms.ohouse.domain.community.model.post.hwpost;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistrictTest {

	@DisplayName("생성자 불변식을 통과한다.")
	@ParameterizedTest
	@CsvSource({"0_2", "1_10", "4_13"})
	void constructor_success(String code) {

		// When
		var district = new District(code, "메시지");
		// Then
		assertThat(district).isNotNull();
	}

	@DisplayName("코드 조건을 만족하지 못할 경우 생성자 불변식을 통과히지 못한다.")
	@ParameterizedTest
	@CsvSource({"0_", "1_", "asdfxcv"})
	void constructor_fail(String code) {
		assertThatThrownBy(() -> {
			new District(code, "메시지");
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@CsvSource({"0_0,서울특별시 강남구", "0,서울특별시"})
	@DisplayName("약속된 양식대로 문자열 값 출력")
	void decodes_code_in_specific_format(String code, String expected) {

		// Given
		var district = new District(code, null);

		// When
		var decoded = district.toString();

		// Then
		assertThat(decoded).isEqualTo(expected);

	}

}
