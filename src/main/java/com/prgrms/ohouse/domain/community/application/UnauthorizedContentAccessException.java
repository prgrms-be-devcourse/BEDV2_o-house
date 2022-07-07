package com.prgrms.ohouse.domain.community.application;

public class UnauthorizedContentAccessException extends RuntimeException {
	public UnauthorizedContentAccessException() {
		super("컨텐츠에 접근할 수 있는 권한이 없습니다.");
	}

	public UnauthorizedContentAccessException(Long contentId, Long userId) {
		super("사용자 " + userId + "는 " + contentId + "에 대한 접근 권한이 없습니다.");
	}
}
