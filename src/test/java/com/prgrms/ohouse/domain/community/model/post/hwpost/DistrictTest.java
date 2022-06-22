package com.prgrms.ohouse.domain.community.model.post.hwpost;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistrictTest {

	@DisplayName("생성자 불변식을 통과한다.")
	@ParameterizedTest()
	@CsvSource({"0_2","1_10","4_13"})
	void constructor_success(String code) {

		// Given

		// When
		var district = new District(code, "메시지");


		// Then
	}


	@DisplayName("생성자 불변식을 통과한다.")
	@ParameterizedTest()
	@CsvSource({"0_","1_","asdfxcv"})

	void constructor_fail(String code) {

		assertThatThrownBy(() -> {
			new District(code, "메시지");
		}).isInstanceOf(IllegalArgumentException.class);

	}



}
