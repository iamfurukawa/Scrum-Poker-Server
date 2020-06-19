package com.scrumpoker.request;

import com.scrumpoker.commons.IssueType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueRequest {

	@ApiModelProperty(value = "Issue name.")
	private String name;

	@ApiModelProperty(value = "Issue link.")
	private String link;

	@ApiModelProperty(value = "Points.")
	private int points;

	@ApiModelProperty(value = "Status.")
	private IssueType status;
}
