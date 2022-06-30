package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;

public interface HousewarmingPostService {
	@Transactional
	Long createPost(Long userId, CreateHousewarmingPostCommand command, List<MultipartFile> images);

	//
	// 삭제
	// 삭제할 권한이 있는지 확인해야 한다.
	// 현재시점에서는 컨텐츠의 작성자로 확인해야 함
	// userid로 비교
	@Transactional
	void deletePost(Long authorId, Long postId);
}
