package com.prgrms.ohouse.domain.community.model.post.question;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.prgrms.ohouse.domain.common.BaseTimeEntity;
import com.prgrms.ohouse.domain.common.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.QuestionPostImage;
import com.prgrms.ohouse.domain.common.file.StoredFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionPost extends BaseTimeEntity implements ImageAttachable {

	public QuestionPost(String content) {
		this.content = content;
	}

	public QuestionPost(String content, List<QuestionPostImage> questionImages) {
		this.content = content;
		this.questionImages = questionImages;
	}

	@Id
	@GeneratedValue
	private Long id;

	//TODO: 검증
	private String content;

	//TODO: 컬렉션 getter로 인한 불변성 붕괴 문제 수정
	@OneToMany(mappedBy = "questionPost")
	private List<QuestionPostImage> questionImages;

	public void setImages(List<StoredFile> storedFiles) {
		this.questionImages = storedFiles.stream()
			.map((f) -> (QuestionPostImage)f)
			.collect(Collectors.toUnmodifiableList());
	}
}