package com.prgrms.ohouse.domain.community.model.housewarming;

// CRUD 주체 : 관리자.
public enum HousingType {

	PRIVATE_ROOM, ONE_ROOM, OFFICETEL, VILLA, APARTMENT, SINGLE_DETACHED, TINY_SINGLE_DETACHED, COMMERCIAL, OFFICE, ETC;

	public static HousingType from(String housingTypeCode) {
		return valueOf(housingTypeCode);
	}

}
