package com.prgrms.ohouse.domain.community.model.question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class QuestionKeyWordCategory {
	// 일반, 부분공정, 가구, 공간, 평수, 실내공간, 기타

	@Id
	@GeneratedValue
	private Short id;

	@Column(nullable = false, unique = true)
	private String name;
}
