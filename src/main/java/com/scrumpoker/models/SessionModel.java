package com.scrumpoker.models;

import com.scrumpoker.commons.DeckType;
import com.scrumpoker.commons.SessionType;
import com.scrumpoker.commons.StatusType;
import com.scrumpoker.entity.Session;
import com.scrumpoker.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class SessionModel {
	
	@Getter
	@ApiModelProperty(hidden = true)
	private long id;

	@Getter
	@Setter
	@ApiModelProperty(value = "Session name.")
	private String name;

	@Getter
	@Setter
	@ApiModelProperty(value = "Max participants allowed in this session.")
	private int maxParticipants;

	@Getter
	@Setter
	@ApiModelProperty(value = "Session type.")
	private SessionType sessionType;

	@Getter
	@Setter
	@ApiModelProperty(hidden = true)
	private StatusType status;

	@Getter
	@Setter
	@ApiModelProperty(value = "Password session.")
	private String password;

	@Getter
	@Setter
	@ApiModelProperty(value = "Deck type.")
	private DeckType deckType;

	@Getter
	@Setter
	@ApiModelProperty(value = "Session URL.")
	private String url;

	@Getter
	@Setter
	@ApiModelProperty(value = "Owner of session.")
	private String owner;
	
	@Getter
	@Setter
	@ApiModelProperty(value = "Number of participants.")
	private long numOfParticipants;
	
	@Getter
	@Setter
	@ApiModelProperty(value = "Number of issues.")
	private long numOfIssues;

	public SessionModel(final Session sessionEntity) {
		this.id = sessionEntity.getId();
		this.name = sessionEntity.getName();
		this.maxParticipants = sessionEntity.getMaxParticipants();
		this.sessionType = sessionEntity.getSessionType();
		this.status = sessionEntity.getStatus();
		this.password = sessionEntity.getPassword();
		this.deckType = sessionEntity.getDeckType();
		this.url = sessionEntity.getUrl();
		this.owner = sessionEntity.getOwner().getEmail();
		this.numOfIssues = sessionEntity.getIssues().size();
		this.numOfParticipants = sessionEntity.getParticipants().size();				
	}
}
