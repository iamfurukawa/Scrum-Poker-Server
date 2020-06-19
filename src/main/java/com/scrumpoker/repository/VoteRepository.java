package com.scrumpoker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scrumpoker.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

	List<Vote> findAllByIssueId(final long issueId);

	Optional<Vote> findByUserIdAndIssueId(final long issueId, final long userId);
}
