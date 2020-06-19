package com.scrumpoker.service;

import java.util.List;

import com.scrumpoker.entity.Issue;
import com.scrumpoker.entity.Session;

public interface IssueService {

	Issue create(final Issue issue, final Session session);
	
	List<Issue> getAllIssuesByUser(final long userId);

	List<Issue> getAllIssuesBySession(final long sessionId);
}