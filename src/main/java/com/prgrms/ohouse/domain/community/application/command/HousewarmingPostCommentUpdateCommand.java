package com.prgrms.ohouse.domain.community.application.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HousewarmingPostCommentUpdateCommand {
	private final Long authorId;
	private final Long commentId;
	private final String comment;
}
