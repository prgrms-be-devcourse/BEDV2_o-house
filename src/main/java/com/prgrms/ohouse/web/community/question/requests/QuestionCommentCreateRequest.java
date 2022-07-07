package com.prgrms.ohouse.web.community.question.requests;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCommentCreateRequest {

	@NotBlank
	@Length(max = 255)
	private String content;
}
