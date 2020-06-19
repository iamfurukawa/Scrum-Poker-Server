package com.scrumpoker.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.scrumpoker.repository.IssueRepository;
import com.scrumpoker.repository.SessionRepository;
import com.scrumpoker.request.IssueRequest;
import com.scrumpoker.response.ErrorResponse;
import com.scrumpoker.response.IssuesResponse;
import com.scrumpoker.service.IssueService;
import com.scrumpoker.service.VoteService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/issues", produces = MediaType.APPLICATION_JSON_VALUE)
public class IssuesController {
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private IssueService issueService;
	
	@PostMapping("/sessions/{sessionId}")
	@ApiOperation(value = "Create an issue by session.")
	public ResponseEntity<?> create(@PathVariable final long sessionId, @RequestBody @Valid Issue issue, final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors(bindingResult)
					.build(), HttpStatus.BAD_REQUEST);
		}

		final Optional<Session> session = sessionRepository.findById(sessionId);

		if (!session.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("SessionNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}

		final Issue issueSaved = issueService.create(issue, session.get());
		return new ResponseEntity<>(issueSaved, HttpStatus.CREATED);
	}
	
	@GetMapping("/sessions/{sessionId}")
	@ApiOperation(value = "Retrieve all issues by session.")
	public IssuesResponse indexBySession(@PathVariable final long sessionId) {
		final IssuesResponse issuesResponse = new IssuesResponse();
		issuesResponse.setIssues(issueService.getAllIssuesBySession(sessionId));
		return issuesResponse;
	}
	
	@GetMapping("/users/{userId}")
	@ApiOperation(value = "Retrieve all issues by users.")
	public IssuesResponse indexByUser(@PathVariable final long userId) {
		final IssuesResponse issuesResponse = new IssuesResponse();
		issuesResponse.setIssues(issueService.getAllIssuesByUser(userId));
		return issuesResponse;
	}
	
	@PutMapping("/{issueId}")
	@ApiOperation(value = "Update an issue.")
	public ResponseEntity<?> update(@PathVariable final long issueId, @RequestBody final IssueRequest issue) {
		final Optional<Issue> issueSaved = issueRepository.findById(issueId);
		
		if(!issueSaved.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("IssueNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		issueSaved.get().setName(issue.getName());
		issueSaved.get().setLink(issue.getLink());
		issueSaved.get().setPoints(issue.getPoints());
		issueSaved.get().setStatus(issue.getStatus());
		
		final Issue issueUpdated = issueRepository.save(issueSaved.get());
		return new ResponseEntity<>(issueUpdated, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{issueId}/sessions/{sessionId}")
	@ApiOperation(value = "Delete an issue by session.")
	public ResponseEntity<?> delete(@PathVariable final long sessionId, @PathVariable final long issueId) {
		final Optional<Session> session = sessionRepository.findById(sessionId);
		
		if(!session.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("SessionNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		if(session.get().getIssues().stream().filter(issue -> issue.getId() == issueId).count() != 1) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("IssueNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		final List<Issue> issuesFiltered = session.get().getIssues().stream()
				.filter(issue -> issue.getId() != issueId)
				.collect(Collectors.toList());
		
		voteService.deleteAllVotesFromIssue(issueId);
		
		session.get().setIssues(issuesFiltered);
		sessionRepository.save(session.get());
		
		issueRepository.deleteById(issueId);
		return ResponseEntity.noContent().build();
	}	
}
