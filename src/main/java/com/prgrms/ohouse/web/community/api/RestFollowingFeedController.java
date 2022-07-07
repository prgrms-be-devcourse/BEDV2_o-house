package com.prgrms.ohouse.web.community.api;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.community.application.FollowingFeedInfoResult;
import com.prgrms.ohouse.domain.community.application.FollowingFeedService;
import com.prgrms.ohouse.web.commerce.results.SliceResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RestFollowingFeedController {

	private final AuthUtility authUtility;
	private final FollowingFeedService followingFeedService;

	@GetMapping("/api/v0/feed")
	public ResponseEntity<SliceResult<FollowingFeedInfoResult>> getFeeds(
		@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return new ResponseEntity<>(new SliceResult<>(
			followingFeedService.getFeedPosts(authUtility.getAuthUser().getId(), pageable)),
			HttpStatus.OK);
	}
}
