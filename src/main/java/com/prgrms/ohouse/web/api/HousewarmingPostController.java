package com.prgrms.ohouse.web.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostService;
import com.prgrms.ohouse.web.requests.HousewarmingPostCreateRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v0/hwpost")
@Slf4j
public class HousewarmingPostController {

	private final HousewarmingPostService postService;

	public HousewarmingPostController(HousewarmingPostService postService) {
		this.postService = postService;
	}

	// multipart 요청은 반드시 POST (PUT 안 된다.)
	@PostMapping
	public Map<String, String> handleCreatePostRequest(
		@RequestPart("payload") @Valid HousewarmingPostCreateRequest payload,
		@RequestPart("image") List<MultipartFile> files) {

		return Map.of("ok", payload.getTitle());
	}

}
