package com.prgrms.ohouse.domain.community.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCommentCreateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCommentUpdateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostCreateCommand;
import com.prgrms.ohouse.domain.community.application.command.HousewarmingPostUpdateCommand;
import com.prgrms.ohouse.domain.community.application.impl.HousewarmingPostServiceImpl;
import com.prgrms.ohouse.domain.community.model.housewarming.Budget;
import com.prgrms.ohouse.domain.community.model.housewarming.Family;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostComment;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostCommentRepository;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.community.model.housewarming.HousingType;
import com.prgrms.ohouse.domain.community.model.housewarming.WorkMetadata;
import com.prgrms.ohouse.domain.community.model.housewarming.WorkerType;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest
@Transactional
class HousewarmingPostServiceImplTest {

	@Autowired
	private HousewarmingPostServiceImpl housewarmingPostServiceImpl;

	@Autowired
	private HousewarmingPostRepository housewarmingPostRepository;

	@Autowired
	private HousewarmingPostCommentRepository commentRepository;

	@Autowired
	private TestDataProvider fixtureProvider;

	@PersistenceContext
	private EntityManager em;

	@MockBean
	private UserAuditorAware userAuditorAware;

	@Test
	@DisplayName("post ?????? ????????? ????????? post??? ???????????? ???????????????.")
	void persist_post_entity_to_database() {
		// TODO: ?????? ?????? ????????? ?????????

		// Given
		var author = fixtureProvider.insertGuestUser("guest");
		var command = HousewarmingPostCreateCommand.builder()
			.title("test1")
			.content("test1content")
			.housingType(HousingType.APARTMENT)
			.area(2L)
			.budget(new Budget(100, 150))
			.family(new Family("SINGLE", null, null))
			.workMetadata(WorkMetadata.builder().workerType(WorkerType.valueOf("SELF")).build())
			.links(Collections.emptyList())
			.build();
		when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(author));

		// When
		Long postId = housewarmingPostServiceImpl.createPost(command, Collections.emptyList());

		// Then
		var createdPost = housewarmingPostRepository.findById(postId);
		assertThat(createdPost).isNotEmpty();
		assertThat(createdPost.get().getUser().getId()).isEqualTo(author.getId());
		assertThat(createdPost.get()).extracting("title").isEqualTo("test1");
		assertThat(createdPost.get().getBudget().getTotal()).isEqualTo(250L);
		assertThat(createdPost.get().getWorkMetadata().getWorkerType()).isEqualTo(WorkerType.SELF);

	}

	@Test
	@DisplayName("???????????? ????????? ?????? ????????? ???????????? ????????????. - ??????")
	void delete_authorized_housewarming_content() {

		// Given
		var persistedUserWithToken = fixtureProvider.insertGuestUser("guest");
		var persistedPost = fixtureProvider.insertHousewarmingPostWithAuthor(
			userAuditorAware, persistedUserWithToken, 1);
		var postId = persistedPost.getId();
		var authorId = persistedPost.getUser().getId();

		// When
		housewarmingPostServiceImpl.deletePost(authorId, postId);

		// Then
		assertThat(housewarmingPostRepository.findById(postId)).isEmpty();
	}

	@Test
	@DisplayName("???????????? ????????? ?????? ?????? ????????? ?????? ?????? ????????? ?????????.")
	void throws_unauthorized_content_exception() {

		// Given
		var persistedUserWithToken = fixtureProvider.insertGuestUser("guest");
		var persistedPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, persistedUserWithToken,
			1);
		var postId = persistedPost.getId();
		var unauthorizedId = persistedPost.getUser().getId() + 4123;

		assertThatThrownBy(() -> {
			housewarmingPostServiceImpl.deletePost(unauthorizedId, postId);
		}).isInstanceOf(UnauthorizedContentAccessException.class);

	}

	@Test
	@DisplayName("postId??? ?????? ????????? ????????? ????????? ????????????.")
	void query_persisted_housewarming_post_by_post_id() {

		// Given
		var author = fixtureProvider.insertGuestUser("guest");
		var savedPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, author, 1);

		// When
		var queriedPostResult = housewarmingPostServiceImpl.getSinglePost(savedPost.getId());

		// Then
		assertThat(queriedPostResult.getPostId()).isEqualTo(savedPost.getId());
		assertThat(queriedPostResult.getContent()).isEqualTo(savedPost.getContent());
		assertThat(queriedPostResult.getImages()).isEqualTo(savedPost.getImages());

	}

	@Test
	@DisplayName("????????? ?????? ????????? ???????????? ???????????? 1 ?????? ?????????.")
	void increment_visit_count_by_1() {

		// Given
		var author = fixtureProvider.insertGuestUser("guest");
		var savedPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, author, 1);

		// When
		housewarmingPostServiceImpl.updateViews(savedPost.getId());
		em.flush();
		em.clear();

		// Then
		var updatedPost = housewarmingPostRepository.findById(savedPost.getId()).orElseThrow();
		assertThat(updatedPost.getVisitCount()).isEqualTo(1);

	}

	@Test
	@DisplayName("???????????? ????????? ?????? ????????? ????????? ??????????????? ???????????? ??????. ")
	void return_proper_post_with_proper_size_page() {
		for (int i = 1; i <= 20; i++) {
			var author = fixtureProvider.insertGuestUser("guest" + i);
			fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, author, i);
			reset(userAuditorAware);
		}

		// Given
		PageRequest pageRequest = PageRequest.of(0, 19);

		// When
		Slice<HousewarmingPostInfoResult> result = housewarmingPostServiceImpl.getPosts(pageRequest);

		// Then
		assertThat(result.getSize()).isEqualTo(19);
		assertThat(result.getContent()).hasSize(19);
		assertThat(result.hasNext()).isTrue();

	}

	@Test
	@DisplayName("???????????? ????????? ????????? ????????? ???????????? ?????? ????????? ????????? ????????????. - ????????? ?????? ??????")
	void authorized_user_update_post_with_information() {

		// Given
		var author = fixtureProvider.insertGuestUser("guest");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, author, 1);
		var updatedContent = "updated1??????1{{image}}??????2{{image}}";
		var updatedTitle = "updatedTitle";
		var command = HousewarmingPostUpdateCommand
			.builder()
			.title(updatedTitle)
			.content(updatedContent).housingType(HousingType.APARTMENT)
			.area(2L)
			.budget(new Budget(100, 150))
			.family(new Family("SINGLE", null, null))
			.workMetadata(WorkMetadata.builder().workerType(WorkerType.valueOf("SELF")).build())
			.links(Collections.emptyList())
			.build();
		List<MultipartFile> imagesToUpdate = List.of(
			new MockMultipartFile("update1", "update1.jpg", "image/jpg", "asdf".getBytes()),
			new MockMultipartFile("updated2", "update2.jpg", "image/jpg", "asdfzxcvc".getBytes())
		);

		// When
		housewarmingPostServiceImpl.updatePost(targetPost.getId(), author.getId(), command, imagesToUpdate);

		// Then
		assertThat(targetPost.getTitle()).isEqualTo(updatedTitle);
		assertThat(targetPost.getContent()).isEqualTo(updatedContent);
		assertThat(targetPost.getImages()).hasSize(2);

	}

	@Test
	@DisplayName("???????????? ???????????? ?????? ????????? ????????????.")
	void user_append_comment_in_housewarming_post() {

		// Given
		var commentAuthor = fixtureProvider.insertGuestUser("guest");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, commentAuthor, 1);
		var command = new HousewarmingPostCommentCreateCommand("??????1", targetPost.getId());
		when(userAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(commentAuthor));

		// When
		var result = housewarmingPostServiceImpl.addComment(command);

		// Then
		assertThat(result).isPositive();

	}

	@Test
	@DisplayName("????????? ????????? ???????????? ????????? ????????? ????????????. - ????????? ????????? ??????")
	void author_of_housewarmingpost_update_its_comment() {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("comAuthor");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, postAuthor, 1);
		HousewarmingPostComment targetComment = fixtureProvider.insertHousewarmingPostCommentWithAuthor(
			userAuditorAware,
			commentAuthor,
			targetPost,
			1);
		var command = new HousewarmingPostCommentUpdateCommand(commentAuthor.getId(), targetComment.getId(), "updated");

		// When
		housewarmingPostServiceImpl.updateComment(command);

		// Then
		var updatedComment = commentRepository.findById(targetComment.getId()).orElseThrow();
		assertThat(updatedComment.getComment()).isEqualTo("updated");

	}

	@Test
	@DisplayName("????????? ????????? ???????????? ?????? ???????????? ????????? ?????? ?????? ????????? ??? ??? ??????.")
	void user_get_denied_when_request_updating_unauthorized_comment() {

		// Given
		var postAuthorNotCommentAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("comAuthor");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, postAuthorNotCommentAuthor,
			1);
		HousewarmingPostComment targetComment = fixtureProvider.insertHousewarmingPostCommentWithAuthor(
			userAuditorAware,
			commentAuthor,
			targetPost,
			1);
		var command = new HousewarmingPostCommentUpdateCommand(postAuthorNotCommentAuthor.getId(),
			targetComment.getId(),
			"updated");

		// when * then
		assertThatThrownBy(() -> {
			housewarmingPostServiceImpl.updateComment(command);
		}).isInstanceOf(UnauthorizedContentAccessException.class);

	}

	@Test
	@DisplayName("????????? ????????? ???????????? ????????? ????????? ????????????. - ????????? ????????? ??????")
	void author_of_housewarmingpost_delete_its_comment() {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("comAuthor");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, postAuthor, 1);
		HousewarmingPostComment targetComment = fixtureProvider.insertHousewarmingPostCommentWithAuthor(
			userAuditorAware,
			commentAuthor,
			targetPost,
			1);

		// When
		housewarmingPostServiceImpl.deleteComment(commentAuthor.getId(), targetComment.getId());

		// Then
		assertThat(commentRepository.findById(targetComment.getId())).isEmpty();
	}

	@Test
	@DisplayName("???????????? ?????? ???????????? ????????? ????????? ????????? ??? ??????.")
	void user_get_denied_when_request_removal_of_other_user_comment() {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("comAuthor");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, postAuthor, 1);
		HousewarmingPostComment targetComment = fixtureProvider.insertHousewarmingPostCommentWithAuthor(
			userAuditorAware,
			commentAuthor,
			targetPost,
			1);
		var otherUser = commentAuthor.getId() + 4444;

		// when & then
		assertThatThrownBy(() -> {
			housewarmingPostServiceImpl.deleteComment(otherUser, targetComment.getId());
		}).isInstanceOf(UnauthorizedContentAccessException.class);
		assertThat(commentRepository.findById(targetComment.getId())).isNotEmpty();

	}

	@Test
	@DisplayName("???????????? ?????? ???????????? ???????????? ????????????.")
	void query_comments_of_housewarming_post() {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("comAuthor");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(userAuditorAware, postAuthor, 1);
		for (int i = 0; i < 30; i++) {
			fixtureProvider.insertHousewarmingPostCommentWithAuthor(userAuditorAware, commentAuthor, targetPost, i);
		}

		// When
		var result = housewarmingPostServiceImpl
			.getCommentsByPostId(PageRequest.of(0, 50), targetPost.getId());

		// Then
		assertThat(result).hasSize(30);
		assertThat(result.hasNext()).isFalse();
		assertThat(result).allMatch(a -> a.getAuthorId().equals(commentAuthor.getId()));

	}

}
