package com.prgrms.ohouse.domain.community.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Budget;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Family;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousingType;
import com.prgrms.ohouse.domain.community.model.post.hwpost.WorkMetadata;
import com.prgrms.ohouse.domain.community.model.post.hwpost.WorkerType;

@SpringBootTest
@Transactional
@DisplayName("HousewarmingPostService는")
class HousewarmingPostServiceTest {

	@Autowired
	private HousewarmingPostService housewarmingPostService;

	@Autowired
	private HousewarmingPostRepository housewarmingPostRepository;

	@Test
	@DisplayName("post 생성 요청을 받아서 post를 생성하고 영속화한다.")
	void persist_post_entity_to_database() {
		// TODO: 선택 필드 테스트 케이스

		// Given
		var command = CreateHousewarmingPostCommand.builder()
			.title("test1")
			.content("test1content")
			.housingType(HousingType.APARTMENT)
			.area(2L)
			.budget(new Budget(100L, 150L))
			.family(new Family("SINGLE", null, null))
			.workMetadata(WorkMetadata.builder().workerType(WorkerType.valueOf("SELF")).build())
			.links(Collections.emptyList())
			.build();

		// When
		Long postId = housewarmingPostService.createPost(command, Collections.emptyList());

		// Then
		var createdPost = housewarmingPostRepository.findById(postId);
		assertThat(createdPost).isNotEmpty();
		assertThat(createdPost.get()).extracting("title").isEqualTo("test1");
		assertThat(createdPost.get().getBudget().getTotal()).isEqualTo(250L);
		assertThat(createdPost.get().getWorkMetadata().getWorkerType()).isEqualTo(WorkerType.SELF);

	}

}
