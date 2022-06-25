package com.prgrms.ohouse.domain.user.model;

public enum GenderType {
	MALE("M"),
	FEMALE("F")
	;

	private final String value;

	GenderType(String value) {
		this.value = value;
	}
}
