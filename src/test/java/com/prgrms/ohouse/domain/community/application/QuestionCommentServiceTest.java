package com.prgrms.ohouse.domain.community.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.application.command.QuestionCommentRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionCommentUpdateCommand;
import com.prgrms.ohouse.domain.community.model.question.QuestionComment;
import com.prgrms.ohouse.domain.community.model.question.QuestionCommentRepository;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest
@Transactional
class QuestionCommentServiceTest {

	@Autowired
	TestDataProvider fixtureProvider;
	@Autowired
	QuestionCommentService commentService;
	@Autowired
	QuestionCommentRepository commentRepository;
	@Autowired
	QuestionPostRepository postRepository;
	@MockBean
	UserAuditorAware userAuditorAware;

	@Nested
	@DisplayName("질문 게시글 댓글 생성 요청을 받으면 ")
	class CreateTest {
		@Nested
		@DisplayName("질문 게시글 댓글 생성에 성공한다: ")
		class SuccessCase {
			@ParameterizedTest
			@CsvSource({"댓글 내용"})
			@DisplayName("사진을 포함하지 않은 경우")
			void withoutPictures(String contents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));

				QuestionPost post = postRepository.save(new QuestionPost("제목", "내용", null, user));

				Long commentId = commentService.createQuestionComment(
					new QuestionCommentRegisterCommand(contents, post.getId(), null));
				QuestionComment comment = commentRepository.findById(commentId).get();

				assertThat(comment.getContents()).isEqualTo(contents);
				assertThat(comment.getAuthor()).isEqualTo(user);
			}

			@ParameterizedTest
			@CsvSource({"댓글 내용"})
			@DisplayName("사진을 포함한 경우")
			void withPictures(String contents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				MockMultipartFile file = new MockMultipartFile(
					"thumbnail",
					"test.png",
					"image/png",
					"<<png data>>".getBytes());
				QuestionPost post = postRepository.save(new QuestionPost("제목", "내용", null, user));

				Long commentId = commentService.createQuestionComment(
					new QuestionCommentRegisterCommand(contents, post.getId(), file));
				QuestionComment comment = commentRepository.findById(commentId).get();

				assertThat(comment.getContents()).isEqualTo(contents);
				assertThat(comment.getAuthor()).isEqualTo(user);
				assertThat(comment.getCommentImage().getOriginalFileName()).isEqualTo("test.png");
			}
		}
	}

	@Nested
	@DisplayName("질문 게시글 댓글 수정 요청을 받으면 ")
	class UpdateTest {
		@Nested
		@DisplayName("질문 게시글 댓글 수정에 성공한다: ")
		class SuccessCase {
			@ParameterizedTest
			@CsvSource({"내용, 변경된 내용"})
			@DisplayName("사진을 포함하지 않은 경우")
			void withoutPictures(String contents, String changedContents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));

				QuestionPost post = postRepository.save(new QuestionPost("제목", "내용", null, user));

				Long commentId = commentService.createQuestionComment(
					new QuestionCommentRegisterCommand(contents, post.getId(), null));

				commentService.editQuestionComment(
					new QuestionCommentUpdateCommand(commentId, changedContents, post.getId(), null), user.getId());
				QuestionComment comment = commentRepository.findById(commentId).get();

				assertThat(comment.getContents()).isEqualTo(changedContents);
				assertThat(comment.getAuthor()).isEqualTo(user);
				assertThat(comment.getCommentImage() == null).isTrue();
			}

			@ParameterizedTest
			@CsvSource({"내용, 변경된 내용"})
			@DisplayName("사진을 포함한 경우")
			void withPictures(String contents, String changedContents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				MockMultipartFile file = new MockMultipartFile(
					"thumbnail",
					"test.png",
					"image/png",
					"<<png data>>".getBytes());
				QuestionPost post = postRepository.save(new QuestionPost("제목", "내용", null, user));

				Long commentId = commentService.createQuestionComment(
					new QuestionCommentRegisterCommand(contents, post.getId(), file));

				MockMultipartFile updatedFile = new MockMultipartFile(
					"thumbnail",
					"test2.png",
					"image/png",
					"<<png data>>".getBytes());

				commentService.editQuestionComment(
					new QuestionCommentUpdateCommand(commentId, changedContents, post.getId(), updatedFile),
					user.getId());
				QuestionComment comment = commentRepository.findById(commentId).get();

				assertThat(comment.getContents()).isEqualTo(changedContents);
				assertThat(comment.getAuthor()).isEqualTo(user);
				assertThat(comment.getCommentImage().getOriginalFileName()).isEqualTo("test2.png");
			}
		}
	}

}