package com.prgrms.ohouse.domain.community.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.UserAuditorAware;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.impl.QuestionPostServiceImpl;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@SpringBootTest
@Transactional
class QuestionPostServiceImplTest {

	@Autowired
	QuestionPostServiceImpl questionPostService;
	@Autowired
	QuestionPostRepository questionPostRepository;
	@MockBean
	UserAuditorAware userAuditorAware;
	@Autowired
	UserRepository userRepository;

	@Nested
	@DisplayName("질문 게시글 생성 요청을 받으면 ")
	class CreateTest {
		@Nested
		@DisplayName("질문 게시글 생성에 성공한다: ")
		class SuccessCase {
			@Test
			@DisplayName("사진을 포함하지 않은 경우")
			void createSuccessWithoutPictures() {
				User user = User.builder()
					.nickname("guestUser")
					.email("guest@gmail.com")
					.password("testPassword12")
					.build();
				userRepository.save(user);
				Mockito.when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(user));

				Long questionPostId = questionPostService.createQuestionPost(new QuestionPostRegisterCommand("내용", null));
				QuestionPost questionPost = questionPostRepository.findById(questionPostId).get();
				assertThat(questionPost.getContent()).isEqualTo("내용");
			}
		}
	}
}