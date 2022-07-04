package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;

public interface HousewarmingPostService {
	Long createPost(Long userId, CreateHousewarmingPostCommand command, List<MultipartFile> images);

	void deletePost(Long authorId, Long postId);

	HousewarmingPostInfoResult getSinglePost(Long postId);

	Slice<HousewarmingPostInfoResult> getPosts(Pageable pageRequest);

	void updateViews(Long postId);
}
