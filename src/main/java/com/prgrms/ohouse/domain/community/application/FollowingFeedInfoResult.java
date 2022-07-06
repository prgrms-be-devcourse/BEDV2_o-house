package com.prgrms.ohouse.domain.community.application;

import java.util.List;
import java.util.stream.Collectors;

import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FollowingFeedInfoResult {

	private Long post_id;

	private PostCategory category;

	private Long author_id;

	private String author_nickname;

	private String title;

	private String content;

	private int visitCount;

	private List<ImageResult> images;

	public static FollowingFeedInfoResult from(HousewarmingPost post) {
		var result = new FollowingFeedInfoResult();
		result.post_id = post.getId();
		result.category = PostCategory.HousewarmingPost;
		result.author_id = post.getUser().getId();
		result.author_nickname = post.getUser().getNickname();
		result.title = post.getTitle();
		result.content = post.getContent();
		result.visitCount = post.getVisitCount();
		result.images = post.getImages().stream().map(ImageResult::from).collect(Collectors.toList());

		return result;
	}
}
