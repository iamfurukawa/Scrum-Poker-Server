package com.scrumpoker.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scrumpoker.entity.Issue;
import com.scrumpoker.entity.Session;
import com.scrumpoker.entity.User;
import com.scrumpoker.repository.IssueRepository;
import com.scrumpoker.repository.SessionRepository;
import com.scrumpoker.repository.UserRepository;
import com.scrumpoker.response.ErrorResponse;
import com.scrumpoker.response.SessionsResponse;
import com.scrumpoker.service.SessionService;
import com.scrumpoker.service.VoteService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionsController {
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private UserRepository userRepository;
		
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/users/{userId}")
	@ApiOperation(value = "Create a session by user.")
	public ResponseEntity<?> create(@PathVariable final long userId, @RequestBody @Valid final Session session, final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors(bindingResult)
					.build(), HttpStatus.BAD_REQUEST);
		}

		final Optional<User> userExists = userRepository.findById(userId);

		if (!userExists.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("UserNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		session.setOwner(userExists.get());
		final Session sessionSaved = sessionService.create(session);
		return new ResponseEntity<>(sessionSaved, HttpStatus.CREATED);
	}

	@GetMapping("/users/{userId}")
	@ApiOperation(value = "Retrieve all sessions by user.")
	public SessionsResponse index(@PathVariable final long userId) {
		final SessionsResponse sessionsResponse = new SessionsResponse();
		sessionsResponse.setSessions(sessionService.getAllSessionsByUser(userId));
		return sessionsResponse;
	}
	
	@PutMapping("/{sessionId}")
	@ApiOperation(value = "Update a session.")
	public ResponseEntity<?> update(@PathVariable final long sessionId,
			@RequestBody final Session session) {
		
		final Optional<Session> sessionSaved = sessionRepository.findById(sessionId);
		
		if(!sessionSaved.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("SessionNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		sessionSaved.get().setDeckType(session.getDeckType());
		sessionSaved.get().setMaxParticipants(session.getMaxParticipants());
		sessionSaved.get().setName(session.getName());
		sessionSaved.get().setPassword(session.getPassword());
		sessionSaved.get().setSessionType(session.getSessionType());
		sessionSaved.get().setUrl(session.getUrl());
		
		final Session sessionUpdated =  sessionRepository.save(sessionSaved.get());
		final SessionsResponse sessionResponse = new SessionsResponse();
		sessionResponse.setSessions(Arrays.asList(sessionUpdated));
		return new ResponseEntity<>(sessionResponse, HttpStatus.CREATED);
	}

	@DeleteMapping("/{sessionId}")
	@ApiOperation(value = "Delete a session.")
	public ResponseEntity<?> delete(@PathVariable final long sessionId) {
		final Optional<Session> session = sessionRepository.findById(sessionId);
		if(!session.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("SessionNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		for (Issue issue : session.get().getIssues()) {
			voteService.deleteAllVotesFromIssue(issue.getId());
		}
		
		session.get().setIssues(Collections.emptyList());
		sessionRepository.save(session.get());
		final List<Issue> issues = session.get().getIssues(); 
		
		for (Issue issue : issues) {
			issueRepository.deleteById(issue.getId());
		}
		
		sessionRepository.deleteById(sessionId);
		return ResponseEntity.noContent().build();
	}
}
