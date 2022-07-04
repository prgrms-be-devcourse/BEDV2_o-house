package com.prgrms.ohouse.domain.community.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
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

import com.prgrms.ohouse.domain.community.application.QuestionPostService;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest
@Transactional
class QuestionPostServiceTest {

	@Autowired
	TestDataProvider fixtureProvider;
	@Autowired
	QuestionPostService questionPostService;
	@Autowired
	QuestionPostRepository questionPostRepository;
	@MockBean
	UserAuditorAware userAuditorAware;

	@Nested
	@DisplayName("질문 게시글 생성 요청을 받으면 ")
	class CreateTest {
		@Nested
		@DisplayName("질문 게시글 생성에 성공한다: ")
		class SuccessCase {
			@ParameterizedTest
			@CsvSource({"제목, 내용", "적당한 제목, 무언가 긴 내용"})
			@DisplayName("사진을 포함하지 않은 경우")
			void withoutPictures(String title, String contents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));

				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand(title, contents, null));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();

				assertThat(questionPost.getTitle()).isEqualTo(title);
				assertThat(questionPost.getContents()).isEqualTo(contents);
				assertThat(questionPost.getAuthor()).isEqualTo(user);
			}

			@ParameterizedTest
			@CsvSource({"제목, 내용", "적당한 제목, 무언가 긴 내용"})
			@DisplayName("사진을 포함한 경우")
			void withPictures(String title, String contents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				MockMultipartFile file = new MockMultipartFile(
					"thumbnail",
					"test.png",
					"image/png",
					"<<png data>>".getBytes());

				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand(title, contents, List.of(file)));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();
				assertThat(questionPost.getTitle()).isEqualTo(title);
				assertThat(questionPost.getContents()).isEqualTo(contents);
				assertThat(questionPost.getAuthor()).isEqualTo(user);
				assertThat(questionPost.getQuestionImages().get(0).getOriginalFileName()).isEqualTo("test.png");
			}
		}
	}

	@Nested
	@DisplayName("질문 게시글 수정 요청을 받으면 ")
	class UpdateTest {
		@Nested
		@DisplayName("질문 게시글 수정에 성공한다: ")
		class SuccessCase {
			@ParameterizedTest
			@CsvSource({"제목, 내용, 변경된 제목, 변경된 내용"})
			@DisplayName("사진을 포함하지 않은 경우")
			void withoutPictures(String title, String contents, String changedTitle, String changedContents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand(title, contents, null));

				questionPostService.editQuestionPost(
					new QuestionPostUpdateCommand(questionPostId, changedTitle, changedContents, null));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();

				assertThat(questionPost.getTitle()).isEqualTo(changedTitle);
				assertThat(questionPost.getContents()).isEqualTo(changedContents);
				assertThat(questionPost.getAuthor()).isEqualTo(user);
				assertThat(questionPost.getQuestionImages().isEmpty()).isTrue();
			}

			@ParameterizedTest
			@CsvSource({"제목, 내용, 변경된 제목, 변경된 내용"})
			@DisplayName("사진을 포함한 경우")
			void withPictures(String title, String contents, String changedTitle, String changedContents) {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				MockMultipartFile previousFile = new MockMultipartFile(
					"thumbnail",
					"test.png",
					"image/png",
					"<<png data>>".getBytes());
				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand(title, contents, List.of(previousFile)));

				MockMultipartFile updatedFile1 = new MockMultipartFile(
					"thumbnail",
					"test2.png",
					"image/png",
					"<<png data>>".getBytes());
				MockMultipartFile updatedFile2 = new MockMultipartFile(
					"thumbnail",
					"test3.png",
					"image/png",
					"<<png data>>".getBytes());

				questionPostService.editQuestionPost(
					new QuestionPostUpdateCommand(questionPostId, changedTitle, changedContents,
						List.of(updatedFile1, updatedFile2)));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();

				assertThat(questionPost.getTitle()).isEqualTo(changedTitle);
				assertThat(questionPost.getContents()).isEqualTo(changedContents);
				assertThat(questionPost.getAuthor()).isEqualTo(user);
				assertThat(questionPost.getQuestionImages().get(0).getOriginalFileName()).isEqualTo("test2.png");
				assertThat(questionPost.getQuestionImages().get(1).getOriginalFileName()).isEqualTo("test3.png");
			}
		}
	}
}