package com.scrumpoker.controller;

import java.util.Optional;

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
import com.scrumpoker.entity.User;
import com.scrumpoker.entity.Vote;
import com.scrumpoker.repository.IssueRepository;
import com.scrumpoker.repository.UserRepository;
import com.scrumpoker.repository.VoteRepository;
import com.scrumpoker.response.ErrorResponse;
import com.scrumpoker.response.VotesResponse;
import com.scrumpoker.service.VoteService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/votes/issues/{issueId}", produces = MediaType.APPLICATION_JSON_VALUE)
public class VotesController {
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VoteService voteService;
	
	@Autowired
	private VoteRepository voteRepository;
	
	@PostMapping("/users/{userId}")
	@ApiOperation(value = "Register a vote by issue and user.")
	public ResponseEntity<?> registerVote(@PathVariable final long issueId, @PathVariable final long userId, @RequestBody final Vote vote, final BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors(bindingResult)
					.build(), HttpStatus.BAD_REQUEST);
		}

		final Optional<Issue> issue = issueRepository.findById(issueId);

		if (!issue.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("IssueNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		final Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("UserNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		vote.setUser(user.get());

		final Vote voteSaved = voteService.create(vote, issue.get());
		return new ResponseEntity<>(voteSaved, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	@ApiOperation(value = "Retrieve all votes by issue.")
	public VotesResponse index(@PathVariable final long issueId) {
		final VotesResponse votesResponse = new VotesResponse();
		votesResponse.setVotes(voteRepository.findAllByIssueId(issueId));
		return votesResponse;
	}
	
	@PutMapping("/users/{userId}")
	@ApiOperation(value = "Update a vote by issue and user.")
	public ResponseEntity<?> update(@PathVariable final long issueId, @PathVariable final long userId, @RequestBody final Vote vote) {
		final Optional<Vote> voteSaved = voteRepository.findByUserIdAndIssueId(userId, issueId);
		
		if(!voteSaved.isPresent()) {
			return new ResponseEntity<>(new ErrorResponse()
					.setErrors("VoteNotFound")
					.build(), HttpStatus.NOT_FOUND);
		}
		
		voteSaved.get().setPoints(vote.getPoints());
		final Vote voteUpdated = voteRepository.save(voteSaved.get());
		
		return new ResponseEntity<>(voteUpdated, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/")
	@ApiOperation(value = "Delete all votes by issue.")
	public ResponseEntity<Void> delete(@PathVariable final long issueId) {
		voteService.deleteAllVotesFromIssue(issueId);
		return ResponseEntity.noContent().build();
	}
}
