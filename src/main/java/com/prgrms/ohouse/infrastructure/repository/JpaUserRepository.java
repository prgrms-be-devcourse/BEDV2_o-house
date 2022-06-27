package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

}
