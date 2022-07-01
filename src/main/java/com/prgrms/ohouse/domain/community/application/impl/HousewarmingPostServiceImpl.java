package com.prgrms.ohouse.domain.community.application.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostService;
import com.prgrms.ohouse.domain.community.application.UnauthorizedContentAccessException;
import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@Service
@Transactional(readOnly = true)
public class HousewarmingPostServiceImpl implements HousewarmingPostService {

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
	public Long createPost(Long userId, CreateHousewarmingPostCommand command, List<MultipartFile> images) {
		User user = userRepository.findById(userId).orElseThrow();
		HousewarmingPost post = command.toPost(user);
		post = housewarmingPostRepository.save(post);
		post.validateContent(images.size());
		fileManager.store(images, post);
		return post.getId();
	}

	@Override
	@Transactional
	public void deletePost(Long authorId, Long postId) {
		HousewarmingPost authorizedPost = getAuthorizedPost(authorId, postId);
		housewarmingPostRepository.delete(authorizedPost);
	}

	@Override
	public HousewarmingPostInfoResult getSinglePost(Long postId) {
		HousewarmingPost post = housewarmingPostRepository.findById(postId).orElseThrow();
		// 성공적으로 가져왔을 경우 조회수를 1 증가시킨다.
		// 트랜잭션이 성공적으로 끝나고 해도 되지 않나? -> 컨트롤러에서 끝내는 케이스
		return HousewarmingPostInfoResult.from(post);
	}

	// @Transactional
	// public int updateViews(Long postId) {
	// 	// housewarmingPostRepository.incrementView(postId);
	// }

	private HousewarmingPost getAuthorizedPost(Long authorId, Long postId) {
		HousewarmingPost hwPost = housewarmingPostRepository.findById(postId).orElseThrow();
		if (!Objects.equals(hwPost.getUser().getId(), authorId)) {
			throw new UnauthorizedContentAccessException();
		}
		return hwPost;
	}
}
