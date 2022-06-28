package com.prgrms.ohouse.domain.community.model.post.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.prgrms.ohouse.domain.common.BaseTimeEntity;
import com.prgrms.ohouse.domain.common.file.ImageAttachable;
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
	@OneToMany(mappedBy = "questionPost", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuestionPostImage> questionImages = new ArrayList<>();

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		QuestionPostImage image = new QuestionPostImage(fileName, fileUrl, this);
		questionImages.add(image);
		return image;
	}
}