package com.prgrms.ohouse.domain.user.model;

import static com.google.common.base.Strings.*;

public enum Gender {
	MALE, FEMALE;

	public static Gender of(String value) {
		if(isNullOrEmpty(value))
			return null;
		//TODO IllegalArguException 구체화
		return Gender.valueOf(value);
	}
}
