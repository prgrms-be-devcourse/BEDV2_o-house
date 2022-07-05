package com.prgrms.ohouse.domain.community.model.question;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QuestionPostKeyWord {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	QuestionPost post;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "keyword_id", nullable = false)
	QuestionKeyWord keyWord;
}
