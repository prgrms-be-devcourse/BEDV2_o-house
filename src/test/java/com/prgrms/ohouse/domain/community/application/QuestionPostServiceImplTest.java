package com.prgrms.ohouse.domain.community.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.UserAuditorAware;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;
import com.prgrms.ohouse.domain.community.application.impl.QuestionPostServiceImpl;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest
@Transactional
class QuestionPostServiceImplTest {

	@Autowired
	TestDataProvider fixtureProvider;
	@Autowired
	QuestionPostServiceImpl questionPostService;
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
			@Test
			@DisplayName("사진을 포함하지 않은 경우")
			void withoutPictures() {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));

				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand("내용", null));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();
				assertThat(questionPost.getContent()).isEqualTo("내용");
				assertThat(questionPost.getAuthor()).isEqualTo(user);
			}

			@Test
			@DisplayName("사진을 포함한 경우")
			void withPictures() {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				MockMultipartFile file = new MockMultipartFile(
					"thumbnail",
					"test.png",
					"image/png",
					"<<png data>>".getBytes());

				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand("내용", List.of(file)));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();
				assertThat(questionPost.getContent()).isEqualTo("내용");
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
			@Test
			@DisplayName("사진을 포함하지 않은 경우")
			void withoutPictures() {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand("내용", null));

				questionPostService.editQuestionPost(new QuestionPostUpdateCommand(questionPostId, "변경된 내용", null));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();

				assertThat(questionPost.getContent()).isEqualTo("변경된 내용");
				assertThat(questionPost.getAuthor()).isEqualTo(user);
				assertThat(questionPost.getQuestionImages().isEmpty()).isTrue();
			}

			@Test
			@DisplayName("사진을 포함한 경우")
			void withPictures() {
				User user = fixtureProvider.insertUser();
				when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));
				MockMultipartFile previousFile = new MockMultipartFile(
					"thumbnail",
					"test.png",
					"image/png",
					"<<png data>>".getBytes());
				Long questionPostId = questionPostService.createQuestionPost(
					new QuestionPostRegisterCommand("내용", List.of(previousFile)));

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

				questionPostService.editQuestionPost(new QuestionPostUpdateCommand(questionPostId, "변경된 내용",
					List.of(updatedFile1, updatedFile2)));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();

				assertThat(questionPost.getContent()).isEqualTo("변경된 내용");
				assertThat(questionPost.getAuthor()).isEqualTo(user);
				assertThat(questionPost.getQuestionImages().get(0).getOriginalFileName()).isEqualTo("test2.png");
				assertThat(questionPost.getQuestionImages().get(1).getOriginalFileName()).isEqualTo("test3.png");
			}
		}
	}
}