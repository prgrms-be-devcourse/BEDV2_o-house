package com.prgrms.ohouse.domain.community.model.question;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class QuestionPostTest {
	@ParameterizedTest
	@CsvSource({"12345678901234567890123456789012345678901234567890123456789, 무언가 긴 내용"})
	@DisplayName("제목의 길이가 30자 초과일 경우 예외가 발생해야 함")
	void tooLongSizeTitle(String title, String contents) {
		assertThrows(IllegalArgumentException.class,
			() -> new QuestionPost(title, contents));
	}

	@ParameterizedTest
	@CsvSource({", 내용"})
	@DisplayName("제목의 길이가 0자일 경우 예외가 발생해야 함")
	void noTitle(String title, String contents) {
		assertThrows(NullPointerException.class,
			() -> new QuestionPost(title, contents));
	}
}