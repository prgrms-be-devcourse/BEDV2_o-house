package com.prgrms.ohouse.domain.community.model.post.hwpost;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Family {

	// 최대 depth 3
	// 이것 또한 enum으로 두기 보다는 관리자가 케어할 수 있는 형태로?

	@Column(name = "family_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;
	@Column(name = "family_description") // description으로 검색하는 경우가 있을까? depth를 더 파기보다는 하나로 퉁치자.
	private String description;
	@Column(name = "family_count")
	private Integer memberCount;

	enum Type {
		SINGLE, MARRIED,CHILDREN, PARENTS,ROOMMATES
	}
}
