package com.prgrms.ohouse.domain.community.model.question;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QuestionKeyWord {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Short id;

	@Column(nullable = false, unique = true)
	private String name;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private QuestionKeyWordCategory category;
}
