package com.scrumpoker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scrumpoker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(final String email);
}
