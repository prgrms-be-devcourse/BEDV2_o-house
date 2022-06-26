package com.prgrms.ohouse.domain.community.model.housewarming;

import java.util.Map;

// CRUD 주체 : 관리자.
public enum HousingType {

	PRIVATE_ROOM, ONE_ROOM, OFFICETEL, VILLA, APARTMENT, SINGLE_DETACHED, TINY_SINGLE_DETACHED, COMMERCIAL, OFFICE, ETC;
	private static final Map<String, HousingType> typeMap = Map.of(
		"1", PRIVATE_ROOM,
		"2", ONE_ROOM,
		"3", OFFICETEL,
		"4", VILLA,
		"5", APARTMENT,
		"6", SINGLE_DETACHED,
		"7", TINY_SINGLE_DETACHED,
		"8", COMMERCIAL,
		"9", OFFICE,
		"10", ETC
	);

	public static HousingType from(String code) {
		return typeMap.get(code);
	}

}
