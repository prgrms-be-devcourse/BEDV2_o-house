package com.prgrms.ohouse.web.community.question.api;

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

import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.community.application.QuestionPostService;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;
import com.prgrms.ohouse.web.community.question.requests.QuestionPostCreateRequest;
import com.prgrms.ohouse.web.community.question.requests.QuestionPostUpdateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestQuestionPostControllerV0 {

	//TODO: 한 계층 더 감싸주기
	private final QuestionPostService questionPostService;
	private final AuthUtility authUtility;

	@PostMapping(value = "/api/v0/questions",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity registerNewQuestionPost(@RequestPart QuestionPostCreateRequest request,
		@RequestPart List<MultipartFile> file) {
		Long questionPostId = questionPostService.createQuestionPost(
			new QuestionPostRegisterCommand(request.getTitle(), request.getContent(), file));
		return ResponseEntity
			.created(URI.create("/api/v0/questions/" + questionPostId))
			.build();
	}

	@PostMapping(value = "/api/v0/questions/{id}",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity editQuestionPost(@RequestPart QuestionPostUpdateRequest request,
		@RequestPart List<MultipartFile> file, @PathVariable("id") Long questionPostId) {
		questionPostService.editQuestionPost(
			new QuestionPostUpdateCommand(questionPostId, request.getTitle(), request.getContent(), file)
		, authUtility.getAuthUser().getId());
		return ResponseEntity
			.ok().build();
	}

	@DeleteMapping("/api/v0/questions/{id}")
	public ResponseEntity deleteQuestionPost(@PathVariable("id") Long postId) {
		questionPostService.deletePost(postId, authUtility.getAuthUser().getId());
		return ResponseEntity.ok().build();
	}
}
