package com.prgrms.ohouse.web.community.housewarming.api;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostService;
import com.prgrms.ohouse.domain.community.application.UnauthorizedContentAccessException;
import com.prgrms.ohouse.web.commerce.results.SliceResult;
import com.prgrms.ohouse.web.community.requests.HousewarmingPostCreateRequest;
import com.prgrms.ohouse.web.user.results.ErrorCode;
import com.prgrms.ohouse.web.user.results.ErrorResult;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v0/hwpost")
@Slf4j
public class HousewarmingPostController {

	private final HousewarmingPostService postService;
	private final AuthUtility authUtility;
	@Value("${app.host}")
	private String host;

	public HousewarmingPostController(HousewarmingPostService postService, AuthUtility authUtility) {
		this.postService = postService;
		this.authUtility = authUtility;
	}

	// multipart 요청은 반드시 POST (PUT 안 된다.)
	@PostMapping
	public ResponseEntity<String> handleCreatePostRequest(
		@RequestPart("payload") @Valid HousewarmingPostCreateRequest payload,
		@RequestPart("image") List<MultipartFile> images) {
		Long postId = postService.createPost(payload.toCommand(), images);

		return ResponseEntity.created(URI.create(host + "api/v0/hwpost/" + postId)).body("post creation success");
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<String> handleDeletePostRequest(@PathVariable Long postId) {
		var user = authUtility.getAuthUser();
		postService.deletePost(user.getId(), postId);
		return ResponseEntity.ok("delete success");
	}

	@GetMapping("/{postId}")
	public ResponseEntity<HousewarmingPostInfoResult> handleGetSinglePostRequest(@PathVariable Long postId) {
		var housewarmingInfoResult = postService.getSinglePost(postId);
		postService.updateViews(postId);
		housewarmingInfoResult.incrementViewCount();
		return ResponseEntity.ok(housewarmingInfoResult);
	}

	@GetMapping("")
	public ResponseEntity<SliceResult<HousewarmingPostInfoResult>> handleGetPostSliceRequest(
		@PageableDefault Pageable pageable) {
		var result = postService.getPosts(pageable);
		return ResponseEntity.ok(new SliceResult<>(result));
	}

	@ExceptionHandler(UnauthorizedContentAccessException.class)
	public ResponseEntity<ErrorResult> handleUnauthorizedException(UnauthorizedContentAccessException exception) {
		log.error(exception.getMessage(), exception);
		return ResponseEntity.status(401).body(ErrorResult.build(ErrorCode.UNAUTHORIZED_REQUEST));
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
		log.error(exception.getMessage(), exception);
		return ResponseEntity.status(404).body("content not found");
	}

}
