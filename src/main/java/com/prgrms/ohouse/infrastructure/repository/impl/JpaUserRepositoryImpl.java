package com.prgrms.ohouse.infrastructure.repository.impl;

import static com.prgrms.ohouse.domain.user.model.QUser.*;

import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.repository.custom.JpaUserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaUserRepositoryImpl implements JpaUserRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public void addFollowingCount(User selectedUser) {
		queryFactory.update(user)
			.set(user.followingCount, user.followingCount.add(1))
			.where(user.id.eq(selectedUser.getId()))
			.execute();
	}

	@Override
	public void addFollowerCount(User selectedUser) {
		queryFactory.update(user)
			.set(user.followerCount, user.followerCount.add(1))
			.where(user.id.eq(selectedUser.getId()))
			.execute();
	}

	@Override
	public void subFollowingCount(User selectedUser) {
		queryFactory.update(user)
			.set(user.followingCount, user.followingCount.subtract(1))
			.where(user.id.eq(selectedUser.getId()))
			.execute();
	}

	@Override
	public void subFollowerCount(User selectedUser) {
		queryFactory.update(user)
			.set(user.followerCount, user.followerCount.subtract(1))
			.where(user.id.eq(selectedUser.getId()))
			.execute();
	}
}
