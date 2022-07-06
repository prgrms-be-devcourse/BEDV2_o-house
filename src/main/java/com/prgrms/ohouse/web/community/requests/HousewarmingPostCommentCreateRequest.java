package com.prgrms.ohouse.web.community.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCommentCreateCommand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HousewarmingPostCommentCreateRequest {
	@NotNull
	private Long postId;
	@NotBlank
	private String comment;

	public HousewarmingPostCommentCreateCommand toCommand() {
		return new HousewarmingPostCommentCreateCommand(comment, postId);
	}
}
