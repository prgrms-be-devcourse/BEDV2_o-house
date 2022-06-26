package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPostRepository;

@Service
public class HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;

	public HousewarmingPostService(HousewarmingPostRepository housewarmingPostRepository) {
		this.housewarmingPostRepository = housewarmingPostRepository;
	}

	public void createPost(CreateHousewarmingPostCommand command, List<MultipartFile> images) {

	}

}
