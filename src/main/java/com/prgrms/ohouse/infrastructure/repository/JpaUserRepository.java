package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.infrastructure.repository.custom.JpaUserRepositoryCustom;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository,
	JpaUserRepositoryCustom {
}
