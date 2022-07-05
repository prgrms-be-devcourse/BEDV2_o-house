package com.prgrms.ohouse.domain.community.application.command;

import java.util.List;

import com.prgrms.ohouse.domain.community.model.housewarming.Budget;
import com.prgrms.ohouse.domain.community.model.housewarming.District;
import com.prgrms.ohouse.domain.community.model.housewarming.Family;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousingType;
import com.prgrms.ohouse.domain.community.model.housewarming.Link;
import com.prgrms.ohouse.domain.community.model.housewarming.WorkMetadata;

public class UpdateHousewarmingPostCommand {
	private String title;
	private String content;
	private HousingType housingType;
	private String housingDescription;
	private Long area;
	private Budget budget;
	private Family family;
	private String company;
	private WorkMetadata workMetadata;
	private String copyrightHolder;
	private List<Link> links;
	private District district;

	public void updatePost(HousewarmingPost post) {
		post.updateMainContent(title, content);
		post.updateExtraDescription(
			housingType,
			housingDescription,
			area,
			budget,
			family,
			company,
			workMetadata,
			copyrightHolder,
			links,
			district);
	}

}
