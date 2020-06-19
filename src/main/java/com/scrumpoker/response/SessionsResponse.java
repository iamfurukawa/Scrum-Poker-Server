package com.scrumpoker.response;

import java.util.List;
import java.util.stream.Collectors;

import com.scrumpoker.commons.StatusType;
import com.scrumpoker.entity.Session;
import com.scrumpoker.models.SessionModel;

import lombok.Getter;

public class SessionsResponse {

	@Getter
	private List<SessionModel> sessions;

	private long sessionsActive;

	private long sessionsDeactivated;
	
	private long totalSessions;
	
	public void setSessions(final List<Session> sessions) {
		this.sessions = sessions.stream()
				.map(SessionModel::new)
				.collect(Collectors.toList());
	}

	public long getSessionsActive() {
		return this.sessions.stream()
				.filter(session -> session.getStatus() == StatusType.ACTIVE)
				.count();
	}

	public long getSessionsDeactive() {
		return this.sessions.stream()
				.filter(session -> session.getStatus() == StatusType.DEACTIVATED)
				.count();
	}
	
	public long getTotalSessions() {
		return this.sessions.size();
	}
}
