package com.scrumpoker.service;

import java.util.List;

import com.scrumpoker.entity.Session;

public interface SessionService {

	Session create(final Session session);

	List<Session> getAllSessionsByUser(final long userId);
}