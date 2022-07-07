package com.prgrms.ohouse.infrastructure.repository.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostCommentInfoResult;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostComment;
import com.prgrms.ohouse.domain.community.model.housewarming.QHousewarmingPostComment;
import com.prgrms.ohouse.infrastructure.repository.custom.JpaHousewarmingPostCommentRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class JpaHousewarmingPostCommentRepositoryImpl implements JpaHousewarmingPostCommentRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final QHousewarmingPostComment qHousewarmingPostComment;

	public JpaHousewarmingPostCommentRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
		this.qHousewarmingPostComment = new QHousewarmingPostComment("comment");
	}

	@Override
	public Slice<HousewarmingPostCommentInfoResult> findSliceByHwPost(Pageable pageable,
		HousewarmingPost housewarmingPost) {
		List<HousewarmingPostComment> comments = queryFactory
			.selectFrom(qHousewarmingPostComment)
			.orderBy(qHousewarmingPostComment.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.where(qHousewarmingPostComment.hwPost.eq(housewarmingPost))
			.fetch();

		return ListDecorator.decorateToSlice(comments, HousewarmingPostCommentInfoResult::from, pageable);
	}
}
