package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPostRepository;

@Service
@Transactional(readOnly = true)
public class HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;
	private final FileManager fileManager;

	public HousewarmingPostService(HousewarmingPostRepository housewarmingPostRepository, FileManager fileManager) {
		this.housewarmingPostRepository = housewarmingPostRepository;
		this.fileManager = fileManager;
	}

	@Transactional
	public Long createPost(CreateHousewarmingPostCommand command, List<MultipartFile> images) {
		// 먼저 영속화해야 연관 관계를 맺을 수 있다.
		HousewarmingPost post = housewarmingPostRepository.save(command.toPost());
		post.validateContent(images.size());
		List<StoredFile> storedFiles = fileManager.store(images, post);
		post.assignImages(storedFiles);

		// TODO: 사용자 id 받은 뒤에 post와 연관 관계 맺기

		return post.getId();
	}

}
