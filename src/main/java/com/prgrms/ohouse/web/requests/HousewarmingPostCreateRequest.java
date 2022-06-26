package com.prgrms.ohouse.web.requests;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

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

	private String familyCount;

	private String company;

	private Integer workDuration;

	private String workUnit;

	private String workTarget;

	@NotNull
	private String workerType;

	private String workerDescription;

	private String copyrightHolder;

	private List<LinkPayload> linkPayloads;
	private Integer RLG;
	private Integer BLG;
	private String districtDescription;

	static class LinkPayload {
		@Length(min = 2000)
		String url;
		String urlDesc;
	}

}
