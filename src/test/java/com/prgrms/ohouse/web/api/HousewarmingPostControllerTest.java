package com.prgrms.ohouse.web.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;

@SpringBootTest
@AutoConfigureMockMvc
class HousewarmingPostControllerTest {

	private static final ObjectMapper json = new ObjectMapper();
	@Autowired
	MockMvc mockMvc;

	@Autowired
	HousewarmingPostRepository postRepository;

	@Value("${app.host}")
	private String host;

	@Value("${file.dir}")
	private String fileDir;

	@Test
	@DisplayName("집들이 게시물 생성 요청을 애플리케이션에 전달한 뒤 생성된 게시물의 URI를 응답한다.")
	void HousewarmingCrateRequest() throws Exception {
		var payload = json.writeValueAsBytes(Map.of(
			"title", "baka",
			"content", "content{{image}}content1{{image}}",
			"housingTypeCode", "PRIVATE_ROOM",
			"area", 2,
			"constructionFee", 200,
			"stylingFee", "200",
			"familyType", "SINGLE",
			"workerType", "SELF"
		));
		// Given
		var payloadPart = new MockMultipartFile(
			"payload",
			"asdf",
			"application/json",
			payload);
		var images =
			List.of(
				new MockMultipartFile("image", "fav.png", "image/png", "chunk1".getBytes()),
				new MockMultipartFile("image", "fav2.png", "image/png", "chunk2".getBytes())
			);
		var multipartRequest = multipart("/api/v0/hwpost");
		images.forEach(multipartRequest::file);
		multipartRequest.file(payloadPart);
		// When
		var result = mockMvc.perform(multipartRequest);

		// Then
		result
			.andDo(print())
			.andExpectAll(
				status().isCreated(),
				header().string("Location", Matchers.matchesRegex(host + "api/v0/hwpost/\\d+$"))
			);
	}

}
