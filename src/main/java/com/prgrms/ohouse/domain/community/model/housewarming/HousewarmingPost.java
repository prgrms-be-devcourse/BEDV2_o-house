package com.prgrms.ohouse.domain.community.model.housewarming;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedBy;

import com.prgrms.ohouse.domain.common.BaseTimeEntity;
import com.prgrms.ohouse.domain.common.file.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HousewarmingPost extends BaseTimeEntity implements ImageAttachable {

	/**
	 * "{{image}}" 이미지의 위치를 지정한 문자열
	 */
	private static final Pattern IMAGE_ESCAPE_PATTERN = Pattern.compile("\\{{2}image\\}{2}");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@CreatedBy
	private User user;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "visit_count")
	private int visitCount;

	@Column(name = "scrap_count")
	private int scrapCount;

	// 주거 형태
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private HousingType housingType;

	@Column(name = "housing_desc")
	private String housingDescription;

	// 평수 : 층수와 상관 없이 연면적을 기준으로 통계를 매기는 것 같다.
	@Column(nullable = false)
	private Long area;

	// 예산(공사/시공 +  홈스타일링 =  총 예산)
	@Embedded
	private Budget budget;

	@Embedded
	private Family family;

	@Column(name = "company")
	private String company;

	@Embedded
	private WorkMetadata workMetadata;

	private String copyrightHolder;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<Link> links = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "housewarmingPost", cascade = CascadeType.REMOVE)
	private List<HousewarmingPostImage> images = new ArrayList<>();

	@Embedded
	private District district;

	@Builder
	public HousewarmingPost(String title, String content, HousingType housingType, String housingDescription,
		Long area, Budget budget, Family family, String company, WorkMetadata workMetadata, String copyrightHolder,
		List<Link> links, District district) {
		assignMainContent(title, content);
		assignExtraData(housingType, housingDescription, area, budget, family, company, workMetadata, copyrightHolder,
			links,
			district);
	}

	private void assignExtraData(HousingType housingType, String housingDescription, Long area, Budget budget,
		Family family,
		String company, WorkMetadata workMetadata, String copyrightHolder, List<Link> links, District district) {
		setHousingType(housingType);
		setHousingDescription(housingDescription);
		setArea(area);
		setBudget(budget);
		setFamily(family);
		setCompany(company);
		setWorkMetadata(workMetadata);
		setCopyrightHolder(copyrightHolder);
		setDistrict(district);
		assignLinks(links);
	}

	private void assignLinks(List<Link> links) {
		for (Link link : links) {
			link.assignPost(this);
		}
		this.links = links;
	}

	private void setCopyrightHolder(String copyrightHolder) {
		this.copyrightHolder = copyrightHolder;
	}

	private void setDistrict(District district) {
		this.district = district;
	}

	private void setHousingDescription(String housingDescription) {
		this.housingDescription = housingDescription;
	}

	private void setHousingType(HousingType housingType) {
		checkNotNull(housingType);
		this.housingType = housingType;
	}

	public void validateImagesInContent(int imageCount) {
		int matchedSequenceCount = 0;
		var matcher = IMAGE_ESCAPE_PATTERN.matcher(content);
		while (matcher.find()) {
			matchedSequenceCount++;
		}

		if (imageCount != matchedSequenceCount)
			throw new InvalidContentFormatException(imageCount, matchedSequenceCount);
	}

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		var image = new HousewarmingPostImage(fileName, fileUrl, this);
		images.add(image);
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.images.clear();
	}

	public void assignMainContent(String title, String content) {
		checkArgument(title.length() > 0);
		checkArgument(content.length() > 0);
		this.title = title;
		this.content = content;
	}

	private void setBudget(Budget budget) {
		checkNotNull(budget);
		this.budget = budget;
	}

	private void setArea(Long area) {
		checkArgument(area > 0, "area는 0보다 커야 합니다.");
		this.area = area;
	}

	private void setFamily(Family family) {
		checkNotNull(family);
		this.family = family;
	}

	private void setCompany(String company) {
		this.company = company;
	}

	private void setWorkMetadata(WorkMetadata workMetadata) {
		checkNotNull(workMetadata);
		this.workMetadata = workMetadata;
	}

	public void assignExtraDescription(HousingType housingType, String housingDescription, Long area, Budget budget,
		Family family, String company, WorkMetadata workMetadata, String copyrightHolder, List<Link> links,
		District district) {
		assignExtraData(housingType, housingDescription, area, budget, family, company, workMetadata, copyrightHolder,
			links, district);
	}
}

