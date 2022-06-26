package com.prgrms.ohouse.domain.community.application.command;

import java.util.List;

import com.prgrms.ohouse.domain.community.model.post.hwpost.Budget;
import com.prgrms.ohouse.domain.community.model.post.hwpost.District;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Family;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousingType;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Link;
import com.prgrms.ohouse.domain.community.model.post.hwpost.WorkMetadata;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateHousewarmingPostCommand {
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

}
