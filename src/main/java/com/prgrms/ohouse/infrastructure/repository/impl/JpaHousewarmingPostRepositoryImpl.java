package com.prgrms.ohouse.infrastructure.repository.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.QHousewarmingPost;
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
}
