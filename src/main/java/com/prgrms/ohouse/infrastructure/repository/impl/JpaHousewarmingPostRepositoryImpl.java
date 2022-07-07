package com.prgrms.ohouse.infrastructure.repository.impl;

import static com.prgrms.ohouse.domain.user.model.QUser.*;
import static com.prgrms.ohouse.domain.user.model.follow.QFollow.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.community.application.FollowingFeedInfoResult;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.QHousewarmingPost;
import com.prgrms.ohouse.domain.user.model.User;

import com.prgrms.ohouse.infrastructure.repository.custom.JpaHousewarmingPostRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class JpaHousewarmingPostRepositoryImpl implements JpaHousewarmingPostRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final QHousewarmingPost qHwPost;

	public JpaHousewarmingPostRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
		this.qHwPost = new QHousewarmingPost("hwPost");
	}

	@Override
	public void incrementViewCount(Long postId) {
		queryFactory
			.update(qHwPost)
			.set(qHwPost.visitCount, qHwPost.visitCount.add(1))
			.where(qHwPost.id.eq(postId))
			.execute();
	}

	@Override
	public Slice<HousewarmingPostInfoResult> findSliceBy(Pageable pageable) {
		List<HousewarmingPost> posts = queryFactory
			.selectFrom(qHwPost)
			.orderBy(qHwPost.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return ListDecorator.decorateToSlice(posts, HousewarmingPostInfoResult::from, pageable);
	}

	@Override
	public Slice<FollowingFeedInfoResult> findSliceByfollowingUser(User fromUser, Pageable pageable) {
		var posts = queryFactory
			.select(qHwPost)
			.from(qHwPost, follow)
			.where(qHwPost.user.id.eq(follow.toUser.id))
			.innerJoin(follow.fromUser, user)
			.on(follow.fromUser.id.eq(fromUser.getId()))
			.orderBy(qHwPost.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return ListDecorator.decorateToSlice(posts, FollowingFeedInfoResult::from, pageable);
	}
}
