package com.prgrms.ohouse.infrastructure.repository.impl;

import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.community.model.housewarming.QHousewarmingPost;
import com.prgrms.ohouse.infrastructure.repository.custom.QueryDSLHousewarmingPostRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class JpaHousewarmingPostRepositoryImpl implements QueryDSLHousewarmingPostRepository {
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
			.execute()
		;

	}
}
