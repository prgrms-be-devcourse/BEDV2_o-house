package com.prgrms.ohouse.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.model.product.Attribute;
import com.prgrms.ohouse.domain.commerce.model.product.AttributeRepository;
import com.prgrms.ohouse.domain.commerce.model.product.Category;
import com.prgrms.ohouse.domain.commerce.model.product.CategoryRepository;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Color;
import com.prgrms.ohouse.domain.commerce.model.product.enums.FourthCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.RootCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.SecondCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Size;
import com.prgrms.ohouse.domain.commerce.model.product.enums.ThirdCategory;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewImage;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.community.model.housewarming.Budget;
import com.prgrms.ohouse.domain.community.model.housewarming.Family;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.community.model.housewarming.HousingType;
import com.prgrms.ohouse.domain.community.model.housewarming.WorkMetadata;
import com.prgrms.ohouse.domain.community.model.housewarming.WorkerType;
import com.prgrms.ohouse.domain.community.model.question.QuestionComment;
import com.prgrms.ohouse.domain.community.model.question.QuestionCommentRepository;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;
import com.prgrms.ohouse.domain.user.model.Address;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.domain.user.model.UserRepository;

@Component
@Transactional
public class TestDataProvider {
	public static final String GUEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBnbWFpbC5jb20iLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2NTYzMTU3NDQsImV4cCI6MTY1ODA0Mzc0NH0.SN55dE55PSha8BpAFP_J6zd113Tnnk2eDF1Ni2Gd53U";
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AttributeRepository attributeRepository;
	@Autowired
	private HousewarmingPostRepository housewarmingPostRepository;
	@Autowired
	private QuestionPostRepository questionPostRepository;
	@Autowired
	private QuestionCommentRepository questionCommentRepository;

	public User insertUser() {
		User user = User.builder()
			.email("test@email.com")
			.nickname("nickname")
			.password("password")
			.followerCount(0)
			.defaultAddress(new Address())
			.build();
		return userRepository.save(user);
	}

	public Category insertCategory() {
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		return categoryRepository.save(category);
	}

	public Attribute insertAttribute() {
		return attributeRepository.save(Attribute.of(Color.BLUE, Size.NORMAL, "brand", Shipping.NORMAL));

	}

	public Product insertProduct() {
		Category category = insertCategory();
		Attribute attribute = insertAttribute();
		Product product = Product.of("product", 2000, "content", category, attribute);
		return productRepository.save(product);
	}

	public List<Product> insert40Product() {
		Category category = insertCategory();
		Attribute attribute = insertAttribute();
		List<Product> result = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			Product product = Product.of("product" + i, 2000, "content" + 1, category, attribute);
			result.add(productRepository.save(product));
		}
		return result;
	}

	public Review insertNormalReview(Product product, User user, int reviewPoint, String contents, int helpPoint) {
		Review review = Review.createReview(product, user, reviewPoint, contents);
		review.increaseHelpPoint(helpPoint);
		return reviewRepository.save(review);
	}

	public Review insertPhotoReview() {
		Product product = insertProduct();
		User user = insertUser();
		Review review = Review.createReview(product, user, 4, "content content content content content content");
		review = reviewRepository.save(review);
		ReviewImage reviewImage = new ReviewImage("testFile", "testUrl", review);
		review.attach("reviewImage", "src/test/resources/static/");
		return review;
	}

	public Review insertPhotoReviewWithUser(User user) {
		userRepository.save(user);
		Product product = insertProduct();
		Review review = Review.createReview(product, user, 4, "content content content content content content");
		review = reviewRepository.save(review);
		ReviewImage reviewImage = new ReviewImage("testFile", "testUrl", review);
		review.attach("reviewImage", "src/test/resources/static/");
		return review;
	}

	public List<Review> insert40PhotoReview() {
		Product product = insertProduct();
		User user = insertUser();
		List<Review> result = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			Review review = Review.createReview(product, user, 4,
				i + "content content content content content content");
			Review entity = reviewRepository.save(review);
			ReviewImage reviewImage = new ReviewImage("testFile", "testUrl", entity);
			entity.attach("reviewImage","src/test/resources/static/");
			result.add(entity);
		}
		return result;
	}

	public List<Review> insert40NormalReview() {
		Product product = insertProduct();
		User user = insertUser();
		List<Review> result = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			Review review = Review.createReview(product, user, 4,
				i + "content content content content content content");
			result.add(reviewRepository.save(review));
		}
		return result;
	}

	public List<Review> insert40NormalReviewWithUser(User user) {
		Product product = insertProduct();
		userRepository.save(user);
		List<Review> result = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			Review review = Review.createReview(product, user, 4,
				i + "content content content content content content");
			result.add(reviewRepository.save(review));
		}
		return result;
	}

	/**
	 * 영속화된 사용자 픽스처 반환
	 * @param nickname : 현정님 jwt 토큰의 nickname은 guest
	 * @return DB에 저장된 User 엔티티(detached)
	 */
	public User insertGuestUser(String nickname) {
		User user = User.builder()
			.email(nickname + "@gmail.com")
			.nickname(nickname)
			.password(nickname)
			.followerCount(0)
			.defaultAddress(new Address())
			.build();
		return userRepository.save(user);
	}

	public HousewarmingPost insertHousewarmingPostWithAuthor(UserAuditorAware aware, User author, int index) {
		when(aware.getCurrentAuditor()).thenReturn(Optional.of(author));
		var savedPost = housewarmingPostRepository.save(
			HousewarmingPost.builder()
				.title("제목" + index)
				.content("내용1{{image}}내용2{{image}}내용3{{image}}")
				.housingType(HousingType.APARTMENT)
				.area(2L)
				.budget(new Budget(100, 150))
				.family(new Family("SINGLE", null, null))
				.workMetadata(WorkMetadata.builder().workerType(WorkerType.valueOf("SELF")).build())
				.links(Collections.emptyList())
				.build());
		reset(aware);
		return savedPost;
	}

	public QuestionPost insertQuestionPostWithUser(UserAuditorAware aware, User author) {
		return insertQuestionPostWithUser(aware, author, "제목", "내용");
	}

	public QuestionPost insertQuestionPostWithUser(UserAuditorAware aware, User author, String title, String contents) {
		when(aware.getCurrentAuditor()).thenReturn(Optional.of(author));
		QuestionPost savedPost = questionPostRepository.save(new QuestionPost(title, contents));
		reset(aware);
		return savedPost;
	}

	public QuestionComment insertQuestionCommentWithUser(UserAuditorAware aware, User author, QuestionPost post) {
		return insertQuestionCommentWithUser(aware, author, post, "댓글 내용");
	}

	public QuestionComment insertQuestionCommentWithUser(UserAuditorAware aware, User author, QuestionPost post, String contents) {
		when(aware.getCurrentAuditor()).thenReturn(Optional.of(author));
		QuestionComment savedComment = questionCommentRepository.save(new QuestionComment(contents, post));
		reset(aware);
		return savedComment;
	}
}

