package com.prgrms.ohouse.infrastructure.repository.impl;

import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.infrastructure.repository.custom.JpaHousewarmingPostRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class JpaHousewarmingPostRepositoryImpl implements JpaHousewarmingPostRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public JpaHousewarmingPostRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}
}
