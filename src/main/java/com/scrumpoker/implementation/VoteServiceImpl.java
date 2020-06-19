package com.scrumpoker.implementation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scrumpoker.entity.Issue;
import com.scrumpoker.entity.Vote;
import com.scrumpoker.repository.IssueRepository;
import com.scrumpoker.repository.VoteRepository;
import com.scrumpoker.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private VoteRepository voteRepository;
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Override
	public Vote create(final Vote vote, final Issue issue) {
		
		vote.setIssue(issue);
		final Vote voteSaved = voteRepository.save(vote);
		issue.addVote(vote);
		issueRepository.save(issue);
		
		return voteSaved;
	}
	
	@Override
	public void deleteAllVotesFromIssue(final long issueId) {
		final Optional<Issue> issue = issueRepository.findById(issueId);
		if(!issue.isPresent()) {
			return;
		}
		
		final List<Long> votesId = issue.get()
				.getVotes().stream()
				.map(Vote::getId)
				.collect(Collectors.toList());
		
		issue.get().setVotes(Collections.emptyList());
		issueRepository.save(issue.get());
		
		for (Long voteId : votesId) {
			voteRepository.deleteById(voteId);
		}
	}
}
