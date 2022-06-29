package com.prgrms.ohouse.web.api;

import static org.assertj.core.api.Assertions.*;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HousewarmingPostControllerTest {

	private static final ObjectMapper json = new ObjectMapper();

	private static final String HW_URL = "/api/v0/hwpost";
	@Autowired
	MockMvc mockMvc;

	@Autowired
	HousewarmingPostRepository postRepository;

	@Autowired
	TestDataProvider fixtureProvider;

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
		var multipartRequest = multipart(HW_URL);
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

	@Test
	@DisplayName("인증 받은 사용자의 삭제 요청을 받으면 삭제 성공 응답을 보내야 한다.")
	void delete_hwpost_with_proper_authorization() throws Exception {

		// Given
		var userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NTYzMTU3NDQsImV4cCI6MTY1ODA0Mzc0NH0.SN55dE55PSha8BpAFP_J6zd113Tnnk2eDF1Ni2Gd53U";
		var post = fixtureProvider.insertHousewarmingPostWithAuthor(fixtureProvider.insertGuestUser("guest"));
		var postId = post.getId();
		// When
		var result = mockMvc.perform(delete(HW_URL + "/" + postId).header("Authorization", userToken));

		// Then
		assertThat(postRepository.findById(postId)).isEmpty();
		result.andExpectAll(
			status().is2xxSuccessful()
		).andDo(print());
	}

	@Test
	@DisplayName("컨텐츠에 대한 권한이 없는 사용자의 삭제 요청을 받으면 권한 없음 응답을 보내야 한다.")
	void deleting_hwpost_fails_with_wrong_authorization_failure_error() throws Exception {

		// Given
		fixtureProvider.insertGuestUser("guest");
		var userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NTYzMTU3NDQsImV4cCI6MTY1ODA0Mzc0NH0.SN55dE55PSha8BpAFP_J6zd113Tnnk2eDF1Ni2Gd53U";
		var savedPost = fixtureProvider
			.insertHousewarmingPostWithAuthor(fixtureProvider.insertGuestUser("guest2"));
		var postId = savedPost.getId();
		// When
		var result = mockMvc.perform(delete(HW_URL + "/" + postId).header("Authorization", userToken));

		// Then
		assertThat(postRepository.findById(postId)).isNotEmpty();
		result.andExpectAll(
			status().isUnauthorized()
		).andDo(print());
	}

}
