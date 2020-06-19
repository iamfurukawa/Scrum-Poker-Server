package com.scrumpoker.service;

import com.scrumpoker.entity.Issue;
import com.scrumpoker.entity.Vote;

public interface VoteService {

	Vote create(final Vote vote, final Issue issue);
	
	void deleteAllVotesFromIssue(final long issueId);
}