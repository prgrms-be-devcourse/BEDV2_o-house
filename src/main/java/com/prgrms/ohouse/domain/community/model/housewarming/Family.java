package com.prgrms.ohouse.domain.community.model.housewarming;

import static com.google.common.base.Preconditions.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Family {

	@Column(name = "family_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(name = "family_description")
	private String description;

	@Column(name = "family_count")
	private Integer familyCount;

	public Family(String typeString, String description, Integer familyCount) {
		checkNotNull(typeString);
		this.type = Type.valueOf(typeString.toUpperCase());
		this.description = description;
		this.familyCount = familyCount;
	}

	public enum Type {
		SINGLE, MARRIED, CHILDREN, PARENTS, ROOMMATES
	}
}
