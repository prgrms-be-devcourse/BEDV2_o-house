package com.prgrms.ohouse.domain.community.application.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HousewarmingPostCommentCreateCommand {
	private final String comment;
	private final Long postId;
}
