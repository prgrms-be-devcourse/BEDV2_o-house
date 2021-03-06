package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCommentCreateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCommentUpdateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCreateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostUpdateCommand;

public interface HousewarmingPostService {
	Long createPost(HousewarmingPostCreateCommand command, List<MultipartFile> images);

	void deletePost(Long authorId, Long postId);

	HousewarmingPostInfoResult getSinglePost(Long postId);

	Slice<HousewarmingPostInfoResult> getPosts(Pageable pageRequest);

	void updateViews(Long postId);

	void updatePost(Long postId, Long authorId, HousewarmingPostUpdateCommand command, List<MultipartFile> images);

	Long addComment(HousewarmingPostCommentCreateCommand command);

	void updateComment(HousewarmingPostCommentUpdateCommand command);

	void deleteComment(Long userId, Long commentId);

	Slice<HousewarmingPostCommentInfoResult> getCommentsByPostId(Pageable pageRequest, Long postId);
}
