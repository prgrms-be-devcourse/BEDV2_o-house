package com.prgrms.ohouse.web.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class HousewarmingPostControllerTest {

	private static final ObjectMapper json = new ObjectMapper();
	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("HousewarmingCreateRequest 성공적 생성 테스트")
	void HousewarmingCrateRequest() throws Exception {
		var image = Files.readAllBytes(Path.of("C:\\Users\\epicb\\Desktop\\test.png"));
		var payload = json.writeValueAsBytes(Map.of(
			"title", "baka",
			"content", "content",
			"housingTypeCode", "2",
			"area", 2,
			"constructionFee", 200,
			"stylingFee", "200",
			"familyType", "SINGLE",
			"workerType", "SELF"

		));
		// Given
		var payloadPart = new MockMultipartFile(
			"payload",
			null,
			"application/json",
			payload);
		var images =
			List.of(
				new MockMultipartFile("image", "fav.png", "image/png", image),
				new MockMultipartFile("image", "fav2.png", "image/png", image)
			);
		var multipartRequest = multipart("/api/v0/hwpost");
		images.forEach(multipartRequest::file);
		multipartRequest.file(payloadPart);
		// When
		var result = mockMvc.perform(multipartRequest);

		// Then
		result
			.andDo(print())
			.andExpect(status().is2xxSuccessful());

	}

}
