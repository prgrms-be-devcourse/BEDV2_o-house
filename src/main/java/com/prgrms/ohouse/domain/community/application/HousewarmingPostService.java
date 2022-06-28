package com.prgrms.ohouse.domain.community.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@Service
@Transactional(readOnly = true)
public class HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;
	private final FileManager fileManager;
	private final UserRepository userRepository;

	public HousewarmingPostService(HousewarmingPostRepository housewarmingPostRepository, FileManager fileManager,
		UserRepository userRepository) {
		this.housewarmingPostRepository = housewarmingPostRepository;
		this.fileManager = fileManager;
		this.userRepository = userRepository;
	}

	@Transactional
	public Long createPost(CreateHousewarmingPostCommand command, List<MultipartFile> images) {
		HousewarmingPost post = housewarmingPostRepository.save(command.toPost());
		post.validateContent(images.size());
		fileManager.store(images, post);
		// TODO: 사용자 id 받은 뒤에 post와 연관 관계 맺기
		return post.getId();
	}

	//
	// 삭제
	// 삭제할 권한이 있는지 확인해야 한다.
	// 현재시점에서는 컨텐츠의 작성자로 확인해야 함
	// userid로 비교
	@Transactional
	public void deletePost(Long authorId, Long postId) {
		// 사용자 엔티티를  불러올 수 있을까?
		HousewarmingPost authorizedPost = getAuthorizedPost(authorId, postId);
		housewarmingPostRepository.delete(authorizedPost);
	}

	// 모든 것을 다 덮어 써야 한다.
	// @Transactional
	// public void updatePost(UpdateHousewarmingPostCommand command, List<MultipartFile> newImages) {
	// 	HousewarmingPost authorizedPost = getAuthorizedPost(command.getAuthorId(), command.getPostId());
	//
	// }

	private HousewarmingPost getAuthorizedPost(Long authorId, Long postId) {
		HousewarmingPost hwPost = housewarmingPostRepository.findById(postId).orElseThrow();
		if (!Objects.equals(hwPost.getUser().getId(), authorId)) {
			throw new UnauthorizedContentAccessException();
		}
		return hwPost;
	}
}
