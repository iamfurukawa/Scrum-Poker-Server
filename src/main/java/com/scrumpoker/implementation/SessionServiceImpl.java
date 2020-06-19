package com.scrumpoker.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scrumpoker.commons.StatusType;
import com.scrumpoker.entity.Session;
import com.scrumpoker.repository.SessionRepository;
import com.scrumpoker.service.SessionService;

@Service
public class SessionServiceImpl implements SessionService {

	@Autowired
	private SessionRepository sessionRepository;

	public Session create(final Session session) {
		final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		final String decryptedPassword = session.getPassword();
		
		session.setStatus(StatusType.ACTIVE);
		session.setPassword(passwordEncoder.encode(decryptedPassword));
		final Session sessionSaved = sessionRepository.save(session);
		sessionSaved.setPassword(decryptedPassword);
		return sessionSaved;
	}

	@Override
	public List<Session> getAllSessionsByUser(final long userId) {
		final List<Session> sessions = sessionRepository.findAllByParticipantsId(userId);
		sessions.addAll(sessionRepository.findAllByOwnerId(userId));
		return sessions.stream()
			     .distinct()
			     .collect(Collectors.toList());
	}
}
