package com.prgrms.ohouse.domain.community.application;

import java.util.List;

import com.prgrms.ohouse.domain.community.model.housewarming.Budget;
import com.prgrms.ohouse.domain.community.model.housewarming.Family;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostImage;
import com.prgrms.ohouse.domain.community.model.housewarming.HousingType;
import com.prgrms.ohouse.domain.community.model.housewarming.WorkMetadata;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class HousewarmingPostInfoResult {

	private Long postId;
	private Long userId;
	private String userNickname;
	private String title;
	private String content;
	private int visitCount;
	private int scrapCount;
	private HousingType housingType;
	private String housingDescription;
	private Long area;
	private Budget budget;
	private Family family;
	private String company;
	private WorkMetadata workMetadata;
	private List<HousewarmingPostImage> images;

	public static HousewarmingPostInfoResult from(HousewarmingPost post) {
		var instance = new HousewarmingPostInfoResult();
		instance.postId = post.getId();
		instance.userId = post.getUser().getId();
		instance.userNickname = post.getUser().getNickname();
		instance.title = post.getTitle();
		instance.content = post.getContent();
		instance.visitCount = post.getVisitCount();
		instance.scrapCount = post.getScrapCount();
		instance.housingType = post.getHousingType();
		instance.housingDescription = post.getHousingDescription();
		instance.area = post.getArea();
		instance.budget = post.getBudget();
		instance.workMetadata = post.getWorkMetadata();
		instance.family = post.getFamily();
		instance.company = post.getCompany();
		instance.images = post.getImages();
		return instance;
	}
}
