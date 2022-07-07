package com.prgrms.ohouse.web.community.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCommentUpdateCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HousewarmingPostCommentUpdateRequest {
	@NotNull
	private Long commentId;
	@NotBlank
	private String comment;

	public HousewarmingPostCommentUpdateCommand toCommand(Long userId) {
		return new HousewarmingPostCommentUpdateCommand(userId, commentId, comment);
	}

}
