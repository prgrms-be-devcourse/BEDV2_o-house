package com.prgrms.ohouse.domain.community.application.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.UnauthorizedContentAccessException;
import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@Service
@Transactional(readOnly = true)
public class HousewarmingPostServiceImpl implements
	com.prgrms.ohouse.domain.community.application.HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;
	private final FileManager fileManager;
	private final UserRepository userRepository;

	public HousewarmingPostServiceImpl(HousewarmingPostRepository housewarmingPostRepository, FileManager fileManager,
		UserRepository userRepository) {
		this.housewarmingPostRepository = housewarmingPostRepository;
		this.fileManager = fileManager;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public Long createPost(CreateHousewarmingPostCommand command, List<MultipartFile> images) {
		HousewarmingPost post = housewarmingPostRepository.save(command.toPost());
		post.validateContent(images.size());
		fileManager.store(images, post);
		// TODO: 사용자 id 받은 뒤에 post와 연관 관계 맺기
		return post.getId();
	}

	@Override
	@Transactional
	public void deletePost(Long authorId, Long postId) {
		HousewarmingPost authorizedPost = getAuthorizedPost(authorId, postId);
		housewarmingPostRepository.delete(authorizedPost);
	}

	private HousewarmingPost getAuthorizedPost(Long authorId, Long postId) {
		HousewarmingPost hwPost = housewarmingPostRepository.findById(postId).orElseThrow();
		if (!Objects.equals(hwPost.getUser().getId(), authorId)) {
			throw new UnauthorizedContentAccessException();
		}
		return hwPost;
	}
}
