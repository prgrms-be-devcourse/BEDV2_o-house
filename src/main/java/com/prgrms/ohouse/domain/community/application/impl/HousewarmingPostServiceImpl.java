package com.prgrms.ohouse.domain.community.application.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostService;
import com.prgrms.ohouse.domain.community.application.UnauthorizedContentAccessException;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCreateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostUpdateCommand;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;

@Service
@Transactional(readOnly = true)
public class HousewarmingPostServiceImpl implements HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;
	private final FileManager fileManager;

	public HousewarmingPostServiceImpl(HousewarmingPostRepository housewarmingPostRepository, FileManager fileManager) {
		this.housewarmingPostRepository = housewarmingPostRepository;
		this.fileManager = fileManager;
	}

	@Override
	@Transactional
	public Long createPost(HousewarmingPostCreateCommand command, List<MultipartFile> images) {
		HousewarmingPost post = command.toPost();
		post = housewarmingPostRepository.save(post);
		post.validateImagesInContent(images.size());
		fileManager.store(images, post);
		return post.getId();
	}

	@Override
	@Transactional
	public void deletePost(Long authorId, Long postId) {
		HousewarmingPost authorizedPost = getAuthorizedPost(authorId, postId);
		fileManager.delete(authorizedPost.getImages(), authorizedPost);
		housewarmingPostRepository.delete(authorizedPost);
	}

	@Override
	public HousewarmingPostInfoResult getSinglePost(Long postId) {
		HousewarmingPost post = housewarmingPostRepository.findById(postId).orElseThrow();
		return HousewarmingPostInfoResult.from(post);
	}

	@Override
	public Slice<HousewarmingPostInfoResult> getPosts(Pageable pageRequest) {
		return housewarmingPostRepository.findSliceBy(pageRequest);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void updateViews(Long postId) {
		housewarmingPostRepository.incrementViewCount(postId);
	}

	@Override
	public void updatePost(Long postId, Long authorId, HousewarmingPostUpdateCommand command,
		List<MultipartFile> newImages) {
		HousewarmingPost post = getAuthorizedPost(authorId, postId);
		command.updatePost(post);
		if (!newImages.isEmpty()) {
			post.validateImagesInContent(newImages.size());
			fileManager.delete(post.getImages(), post);
			fileManager.store(newImages, post);
		}
	}

	private HousewarmingPost getAuthorizedPost(Long authorId, Long postId) {
		HousewarmingPost hwPost = housewarmingPostRepository.findById(postId).orElseThrow();
		if (!Objects.equals(hwPost.getUser().getId(), authorId)) {
			throw new UnauthorizedContentAccessException();
		}
		return hwPost;
	}

}
