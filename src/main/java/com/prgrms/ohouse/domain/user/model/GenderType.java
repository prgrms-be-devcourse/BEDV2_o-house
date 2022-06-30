package com.prgrms.ohouse.domain.user.model;

import static com.google.common.base.Strings.*;

public enum GenderType {
	MALE, FEMALE;

	public static GenderType of(String value) {
		if(isNullOrEmpty(value))
			return null;
		//TODO IllegalArguException 구체화
		return GenderType.valueOf(value);
	}
}
