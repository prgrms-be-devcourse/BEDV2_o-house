package com.prgrms.ohouse.domain.common.file;

/**
 * 이미지를 붙일 수 있는 타입입니다.
 * attach(String fileName, String fileUrl)메서드로 이미지 객체와 연관관계를 맺어주며,
 * 해당 메서드의 호출은 FileManager가 수행합니다.
 */
public interface ImageAttachable {
	StoredFile attach(String fileName, String fileUrl);
}