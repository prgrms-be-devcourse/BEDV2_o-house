package com.prgrms.ohouse.web.community.question.api;

import static org.springframework.http.MediaType.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.community.application.QuestionCommentService;
import com.prgrms.ohouse.domain.community.application.command.QuestionCommentRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionCommentUpdateCommand;
import com.prgrms.ohouse.web.community.question.requests.QuestionCommentCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestQuestionCommentControllerV0 {

	private final QuestionCommentService commentService;
	private final AuthUtility authUtility;

	@PostMapping(value = "/api/v0/questions/{postId}/comments",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity registerNewQuestionPost(@RequestPart QuestionCommentCreateRequest request,
		@RequestPart MultipartFile file, @PathVariable("postId") Long postId) {
		Long commentId = commentService.createQuestionComment(
			new QuestionCommentRegisterCommand(request.getContent(), postId, file));
		return ResponseEntity
			.created(URI.create("/api/v0/questions/" + commentId))
			.build();
	}

	@PostMapping(value = "/api/v0/questions/{postId}/{commentId}",
		consumes = {APPLICATION_JSON_VALUE, MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity editQuestionPost(@RequestPart QuestionCommentCreateRequest request,
		@RequestPart MultipartFile file, @PathVariable("postId") Long postId,
		@PathVariable("commentId") Long commentId) {
		commentService.editQuestionComment(
			new QuestionCommentUpdateCommand(commentId, request.getContent(), postId, file)
		, authUtility.getAuthUser().getId());
		return ResponseEntity
			.ok(URI.create("/api/v0/" + postId + "/" + commentId));
	}

	//TODO: 권한 추가
	@DeleteMapping("/api/v0/questions/{postId}/{commentId}")
	public ResponseEntity deleteQuestionPost(@PathVariable("postId") Long postId,
		@PathVariable("commentId") Long commentId) {
		commentService.deleteComment(commentId, authUtility.getAuthUser().getId());
		return ResponseEntity.ok().build();
	}
}