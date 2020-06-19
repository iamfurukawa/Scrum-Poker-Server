package com.scrumpoker.models;

import com.scrumpoker.entity.Vote;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class VoteModel {

	@Getter
	@ApiModelProperty(value = "Vote id.")
	private long id;

	@Getter
	@Setter
	@ApiModelProperty(value = "Voted by user.")
	private String userName;
	
	@Getter
	@Setter
	@ApiModelProperty(value = "Voted by user.")
	private long userId;

	@Getter
	@Setter
	@ApiModelProperty(value = "Issue Id.")
	private long issueId;

	@Getter
	@Setter
	@ApiModelProperty(value = "Points given for an issue.")
	private int points;
	
	public VoteModel(final Vote vote) {
		this.id = vote.getId();
		this.userName = vote.getUser().getFirstName() + " " + vote.getUser().getLastName();
		this.userId = vote.getUser().getId();
		this.issueId = vote.getIssue().getId();
		this.points = vote.getPoints();
	}
}
