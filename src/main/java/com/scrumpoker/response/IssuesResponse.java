package com.scrumpoker.response;

import java.util.List;

import com.scrumpoker.entity.Issue;

import lombok.Getter;
import lombok.Setter;

public class IssuesResponse {
	
	@Getter
	@Setter	
	private List<Issue> issues;

	private int totalIssues;
	
	public int getTotalIssues() {
		return this.issues.size();
	}
	
}
