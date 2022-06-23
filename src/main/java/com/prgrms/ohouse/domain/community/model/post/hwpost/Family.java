package com.prgrms.ohouse.domain.community.model.post.hwpost;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Family {

	@Column(name = "family_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(name = "family_description")
	private String description;

	@Column(name = "family_count")
	private Integer memberCount;

	enum Type {
		SINGLE, MARRIED, CHILDREN, PARENTS, ROOMMATES
	}
}
