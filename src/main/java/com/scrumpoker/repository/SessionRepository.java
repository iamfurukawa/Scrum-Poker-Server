package com.scrumpoker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scrumpoker.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long>{
	
	List<Session> findAllByParticipantsId(final Long userId);
	List<Session> findAllByOwnerId(final Long userId);
}
