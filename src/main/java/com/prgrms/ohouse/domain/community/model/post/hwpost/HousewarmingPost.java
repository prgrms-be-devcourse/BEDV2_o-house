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

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "visit_count")
	private int visitCount;

	@Column(name = "scrap_count")
	private int scrapCount;

	// 주거 형태
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private HousingType housingType;

	@Column(name = "housing_description")
	private String housingDescription;

	// 평수 : 단층, 2층 단독/협소 주택, 3층 이상 단독/협소 주택 => 층 별 통합해서 저장하고 출력하는 것 같다.
	// TODO: 층까지 저장하는 값 타이 고려해보기
	@Column(nullable = false)
	private Long area;

	// 예산(공사/시공 +  홈스타일링 =  총 예산)
	// 사이트에서는 저장과 출력을 합산으로 계산하는데 따로 저장해야 하는가?
	@Embedded
	private Budget budget;

	@Embedded
	private Family family;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Worker worker;

	@Column(name = "worker_desc")
	private String workerDescription;

	@Column(name = "work_detail")
	@Enumerated(EnumType.STRING)
	private WorkDetail workDetail;

	@Column(name = "company")
	private String company;

	@Embedded
	private WorkDuration workDuration;

	private String copyright;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
	private List<Link> links = new ArrayList<>();

	@Embedded
	private District district;
}

