package com.prgrms.ohouse.domain.community.application;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FollowingFeedInfoResult {

	private Long postId;

	private PostCategory category;

	private Long authorId;

	private String authorNickname;

	private String title;

	private String content;

	private int visitCount;

	private List<ImageResult> images;

	public static FollowingFeedInfoResult from(HousewarmingPost post) {
		var result = new FollowingFeedInfoResult();
		result.postId = post.getId();
		result.category = PostCategory.HousewarmingPost;
		result.authorId = post.getUser().getId();
		result.authorNickname = post.getUser().getNickname();
		result.title = post.getTitle();
		result.content = post.getContent();
		result.visitCount = post.getVisitCount();
		result.images = post.getImages().stream().map(ImageResult::from).collect(Collectors.toList());

		return result;
	}
}
