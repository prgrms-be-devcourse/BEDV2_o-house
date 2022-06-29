package com.prgrms.ohouse.domain.user.model;

public enum GenderType {
	MALE("M"),
	FEMALE("F")
	;

	private final String value;

	GenderType(String value) {
		this.value = value;
	}

	public static GenderType of(String value) {
		if(value.equals(MALE.value))
			return MALE;
		else if(value.equals(FEMALE.value))
			return FEMALE;
		else
			throw new IllegalArgumentException();
	}
}
