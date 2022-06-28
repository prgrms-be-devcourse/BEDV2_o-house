package com.prgrms.ohouse.web.api.v0;

import static org.springframework.http.MediaType.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.model.post.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.post.question.QuestionPostService;
import com.prgrms.ohouse.domain.community.model.post.question.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.web.requests.QuestionPostCreateRequest;
import com.prgrms.ohouse.web.responses.QuestionPostResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestQuestionPostControllerV0 {

	//TODO: 한 계층 더 감싸주기
	private final QuestionPostService questionPostService;

	@PostMapping(value = "/api/v0/questions",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<QuestionPostResponse> registerNewQuestionPost(@RequestPart QuestionPostCreateRequest request,
		@RequestPart List<MultipartFile> file) {
		QuestionPost questionPost = questionPostService.createQuestionPost(
			new QuestionPostRegisterCommand(request.getContent(), file));
		return ResponseEntity
			.created(URI.create("/api/v0/questions/" + questionPost.getId()))
			.body(QuestionPostResponse.of(questionPost));
	}

	@DeleteMapping("/api/v0/questions/{id}")
	public ResponseEntity<Object> deleteQuestionPost(@PathVariable("id") Long postId) {
		questionPostService.deletePost(postId);
		return ResponseEntity.ok().build();
	}
}