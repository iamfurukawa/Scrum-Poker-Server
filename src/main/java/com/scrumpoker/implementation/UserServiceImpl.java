package com.scrumpoker.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scrumpoker.commons.StatusType;
import com.scrumpoker.entity.User;
import com.scrumpoker.repository.UserRepository;
import com.scrumpoker.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User create(final User user) {
		final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		final String decryptedPassword = user.getPassword();
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setStatus(StatusType.ACTIVE);
		final User userSaved = userRepository.save(user);
		
		userSaved.setPassword(decryptedPassword);
		return userSaved;
	}
	
	public Optional<User> findByEmail(final String email) {
		return userRepository.findByEmail(email);
	}
}
