package com.prgrms.ohouse.domain.community.application;

import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostComment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class HousewarmingPostCommentInfoResult {
	private final Long postId;
	private final Long authorId;
	private final Long commentId;
	private final String comment;

	public static HousewarmingPostCommentInfoResult from(HousewarmingPostComment comment) {
		return new HousewarmingPostCommentInfoResult(
			comment.getHwPost().getId(),
			comment.getAuthor().getId(),
			comment.getId(),
			comment.getComment());
	}
}
