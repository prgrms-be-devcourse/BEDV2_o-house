package com.prgrms.ohouse.domain.community.application;

import org.springframework.stereotype.Service;

import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPostRepository;

@Service
public class HousewarmingPostService {

	private final HousewarmingPostRepository housewarmingPostRepository;

	public HousewarmingPostService(HousewarmingPostRepository housewarmingPostRepository) {
		this.housewarmingPostRepository = housewarmingPostRepository;
	}
	//
	// // 테스트한다면 데이터베이스의 상태 검증
	// //
	// public void createPost(CreateHousewarmingPostCommand command) {
	// 	var builder = HousewarmingPost.builder();
	// 	var post = builder
	// 		.area(command.getArea())
	// 		.budget(command.getBudget())
	// 		.company(command.getCompany())
	// 		.content(command.getContent())
	// 		.copyrightHolder(command.getCopyrightHolder())
	// 		.district(command.getDistrict())
	// 		.family(command.getFamily())
	// 		.housingDescription(command.getHousingDescription())
	// 		.housingType(command.getHousingType())
	// 		.title(command.getTitle())
	// 		.workMetadata(command.getWorkMetadata())
	// 		.build();
	//
	// }

}
