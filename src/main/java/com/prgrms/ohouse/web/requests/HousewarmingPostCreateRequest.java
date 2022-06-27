package com.prgrms.ohouse.web.requests;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.prgrms.ohouse.domain.community.application.command.CreateHousewarmingPostCommand;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Budget;
import com.prgrms.ohouse.domain.community.model.post.hwpost.District;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Family;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousingType;
import com.prgrms.ohouse.domain.community.model.post.hwpost.Link;
import com.prgrms.ohouse.domain.community.model.post.hwpost.WorkMetadata;
import com.prgrms.ohouse.domain.community.model.post.hwpost.WorkTarget;
import com.prgrms.ohouse.domain.community.model.post.hwpost.WorkerType;

import lombok.Getter;

/*
	입력값 기본 검증, 아마도 formdata로 넘어올 것 같다.
 */
@Getter
public class HousewarmingPostCreateRequest {
	@NotBlank(message = "제목을 반드시 작성해야 합니다.")
	private String title;

	@NotBlank(message = "내용을 반드시 작성해야 합니다.")
	private String content;

	@Pattern(regexp = "^[1-9]\\d*$")
	private String housingTypeCode;

	private String housingDescription;

	@NotNull
	private Long area;

	@NotNull
	private Long constructionFee;

	@NotNull
	private Long stylingFee;

	@NotNull
	private String familyType;

	private String familyDescription;

	private Integer familyCount;

	private String company;

	private Integer workDuration;

	private String workUnit;

	private String workTarget;

	@NotNull
	private String workerType;

	private String workerDescription;

	private String copyrightHolder;

	private List<LinkPayload> linkPayloads;

	private String districtCode;

	private String districtDescription;

	public CreateHousewarmingPostCommand toCommand() {
		HousingType housingType = HousingType.from(housingTypeCode);
		Budget budget = new Budget(constructionFee, stylingFee);
		Family family = new Family(familyType, familyDescription, familyCount);
		District district = new District(districtCode, districtDescription);
		WorkMetadata workMetadata = WorkMetadata.builder()
			.unit(mapToEnumOrNull(WorkMetadata.Unit.class, workUnit))
			.duration(workDuration)
			.workerDescription(workerDescription)
			.workTarget(mapToEnumOrNull(WorkTarget.class, workTarget))
			.workerType(WorkerType.valueOf(workerType))
			.build();
		List<Link> links = convertToLinks();

		return CreateHousewarmingPostCommand.builder()
			.title(title)
			.content(content)
			.housingType(housingType)
			.housingDescription(housingDescription)
			.area(area)
			.budget(budget)
			.family(family)
			.company(company)
			.workMetadata(workMetadata)
			.copyrightHolder(copyrightHolder)
			.links(links)
			.district(district)
			.build();
	}

	private List<Link> convertToLinks() {
		return linkPayloads == null
			? Collections.emptyList()
			: linkPayloads.stream()
			.map(linkPayload -> new Link(linkPayload.url, linkPayload.urlDesc))
			.collect(
				Collectors.toUnmodifiableList());
	}

	private <T extends Enum<T>> T mapToEnumOrNull(Class<T> enumType, String name) {
		return name == null ? null : Enum.valueOf(enumType, name);
	}

	static class LinkPayload {
		@Length(min = 2000)
		String url;
		String urlDesc;
	}

}
