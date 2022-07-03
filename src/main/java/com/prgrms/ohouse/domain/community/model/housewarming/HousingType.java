package com.prgrms.ohouse.domain.community.model.housewarming;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

// CRUD 주체 : 관리자.
public enum HousingType {

	PRIVATE_ROOM, ONE_ROOM, OFFICETEL, VILLA, APARTMENT, SINGLE_DETACHED, TINY_SINGLE_DETACHED, COMMERCIAL, OFFICE, ETC;

	@JsonCreator
	public static HousingType from(String housingTypeCode) {
		return valueOf(housingTypeCode.toUpperCase());
	}

	@JsonValue
	public String getSerializedValue() {
		return this.name().toLowerCase();
	}

}
