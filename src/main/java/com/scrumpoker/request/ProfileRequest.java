package com.scrumpoker.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequest {

	@ApiModelProperty(value = "New first name.")
	private String firstName;
	
	@ApiModelProperty(value = "New last name.")
	private String lastName;
	
	@ApiModelProperty(value = "New email.")
	private String email;
}
