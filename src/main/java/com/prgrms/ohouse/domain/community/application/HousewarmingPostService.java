package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPostRepository;

@Service
@Transactional(readOnly = true)
public class HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;

	public HousewarmingPostService(HousewarmingPostRepository housewarmingPostRepository) {
		this.housewarmingPostRepository = housewarmingPostRepository;
	}

	@Transactional
	public Long createPost(CreateHousewarmingPostCommand command, List<MultipartFile> images) {

		HousewarmingPost post = command.toPost();

		// TODO: 이미지 파싱 및 연관관계 맺기
		// TODO: 사용자 id 받은 뒤에 post와 연관 관계 맺기

		post = housewarmingPostRepository.save(post);
		return post.getId();
	}

}
