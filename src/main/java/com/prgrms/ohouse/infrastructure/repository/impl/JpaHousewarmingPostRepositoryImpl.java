package com.prgrms.ohouse.infrastructure.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
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

		var postDtos = posts.stream()
			.map(HousewarmingPostInfoResult::from)
			.collect(Collectors.toList());
		var hasNext = pageable.getPageSize() + 1 == postDtos.size();
		if (hasNext) {
			postDtos.remove(postDtos.size() - 1);
		}
		return new SliceImpl<>(
			Collections.unmodifiableList(postDtos),
			PageRequest.of(pageable.getPageNumber(), postDtos.size()),
			hasNext);
	}
}
