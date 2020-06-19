package com.scrumpoker.controller;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scrumpoker.entity.User;
import com.scrumpoker.repository.UserRepository;
import com.scrumpoker.request.PasswordRequest;
import com.scrumpoker.request.ProfileRequest;
import com.scrumpoker.response.ErrorResponse;
import com.scrumpoker.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/")
	@ApiOperation(value = "Add a new user.")
	public ResponseEntity<?> create(@RequestBody @Valid final User user, final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors(bindingResult)
					.build(), HttpStatus.BAD_REQUEST);
		}

		final Optional<User> userExists = userService.findByEmail(user.getEmail());

		if (userExists.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("UserAlreadyExists")
					.build(), HttpStatus.BAD_REQUEST);
		}

		final User userSaved = userService.create(user);
		return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
	}
	
	@PutMapping("/{userId}/profile")
	@ApiOperation(value = "Update a profile.")
	public ResponseEntity<?> updateProfile(@PathVariable final long userId, @RequestBody final ProfileRequest profile) {
		final Optional<User> userCreated = userRepository.findById(userId);
		if(!userCreated.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("UserNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		final Optional<User> userIsUsed = userRepository.findByEmail(profile.getEmail());
		final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		if(userIsUsed.isPresent() && !auth.getName().equals(profile.getEmail())) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("UserAlreadyExists")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		userCreated.get().setFirstName(profile.getFirstName());
		userCreated.get().setLastName(profile.getLastName());
		userCreated.get().setEmail(profile.getEmail());
		
		final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    final Validator validator = factory.getValidator();
	    final Set<ConstraintViolation<User>> constraintViolations = validator.validate(userCreated.get());
	    
	    if (!constraintViolations.isEmpty()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors(constraintViolations.stream()
					.map(ConstraintViolation::getMessage)
					.collect(Collectors.toList()))
					.build(), HttpStatus.BAD_REQUEST);
		}
		
		final User userUpdated = userRepository.save(userCreated.get());
		return new ResponseEntity<>(userUpdated, HttpStatus.CREATED);
	}

	@PutMapping("/{userId}/password")
	@ApiOperation(value = "Update a password.")
	public ResponseEntity<?> updatePassword(@PathVariable final long userId, @RequestBody final PasswordRequest password) {
		final Optional<User> userCreated = userRepository.findById(userId);
		if(!userCreated.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("UserNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		userCreated.get().setPassword(passwordEncoder.encode(password.getPassword()));
		final User userUpdated = userRepository.save(userCreated.get());
		return new ResponseEntity<>(userUpdated, HttpStatus.CREATED);
	}
}
