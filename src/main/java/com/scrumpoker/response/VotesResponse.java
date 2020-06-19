package com.scrumpoker.response;

import java.util.List;
import java.util.stream.Collectors;

import com.scrumpoker.entity.Vote;
import com.scrumpoker.models.VoteModel;

import lombok.Getter;

@Getter
public class VotesResponse {
	
	private List<VoteModel> votes;
	
	public void setVotes(List<Vote> votes) {
		this.votes = votes.stream()
				.map(VoteModel::new)
				.collect(Collectors.toList());
	}
}
