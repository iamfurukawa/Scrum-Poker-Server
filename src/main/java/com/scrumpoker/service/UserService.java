package com.scrumpoker.service;

import java.util.Optional;

import com.scrumpoker.entity.User;

public interface UserService {
	
	User create(final User user);
	
	Optional<User> findByEmail(final String email);
}