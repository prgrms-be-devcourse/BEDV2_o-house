package com.prgrms.ohouse.web.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.common.security.AuthUtils;
import com.prgrms.ohouse.domain.user.application.FollowService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0")
public class FollowRestController {
	private final FollowService followService;

	@PostMapping("/user/{userId}/follow")
	public ResponseEntity<String> follow(@PathVariable("userId") Long userId){
		followService.followUser(AuthUtils.getAuthUser().getId(), userId);
		return ResponseEntity.ok().body("User Follow Success");
	}

	@DeleteMapping("/user/{userId}/follow")
	public ResponseEntity<String> unfollow(@PathVariable("userId") Long userId){
		followService.unfollowUser(AuthUtils.getAuthUser().getId(), userId);
		return ResponseEntity.ok().body("User Unfollow Success");
	}
}
