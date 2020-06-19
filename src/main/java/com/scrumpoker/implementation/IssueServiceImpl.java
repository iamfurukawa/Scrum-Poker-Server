package com.scrumpoker.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scrumpoker.commons.IssueType;
import com.scrumpoker.entity.Issue;
import com.scrumpoker.entity.Session;
import com.scrumpoker.repository.IssueRepository;
import com.scrumpoker.repository.SessionRepository;
import com.scrumpoker.service.IssueService;

@Service
public class IssueServiceImpl implements IssueService {
	
	@Autowired
	private IssueRepository issueRepository;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Override
	public Issue create(final Issue issue, final Session session) {
		issue.setStatus(IssueType.IN_VOTE);
		final Issue issueSaved = issueRepository.save(issue);
		
		session.addIssue(issueSaved);
		sessionRepository.save(session);
		
		return issueSaved;
	}
	
	@Override
	public List<Issue> getAllIssuesByUser(final long userId) {
		final List<Session> sessions = sessionRepository.findAllByParticipantsId(userId);
		sessions.addAll(sessionRepository.findAllByOwnerId(userId));
		final List<Issue> issues = new ArrayList<>();
		
		for (Session session : sessions) {
			issues.addAll(session.getIssues());
		}
		
		final Set<Long> issueIds = issues.stream()
				.map(Issue::getId)
				.collect(Collectors.toSet());
		
		return issueRepository.findAllById(issueIds);
	}

	@Override
	public List<Issue> getAllIssuesBySession(long sessionId) {
		final Optional<Session> session = sessionRepository.findById(sessionId);
		if (session.isPresent()) {
			return session.get().getIssues();
		}
		return Collections.emptyList();
	}
}
