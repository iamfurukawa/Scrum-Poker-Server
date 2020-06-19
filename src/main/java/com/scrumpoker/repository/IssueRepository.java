package com.scrumpoker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scrumpoker.entity.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long>{

}
