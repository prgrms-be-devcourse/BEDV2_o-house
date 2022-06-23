package com.prgrms.ohouse.domain.community.model.post.hwpost;

import java.util.ArrayList;
import java.util.List;

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

import com.prgrms.ohouse.user.domain.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HousewarmingPost {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private List<Link> links = new ArrayList<>();

	@Embedded
	private District district;
}

