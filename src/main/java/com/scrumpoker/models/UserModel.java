package com.scrumpoker.models;

import com.scrumpoker.commons.StatusType;
import com.scrumpoker.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class UserModel {
	
	@Getter
	@ApiModelProperty(hidden = true)
	private long id;

	@Getter
	@Setter
	@ApiModelProperty(value = "User first name.")
	private String firstName;

	@Getter
	@Setter
	@ApiModelProperty(value = "User last name.")
	private String lastName;
	
	@Getter
	@Setter
	@ApiModelProperty(value = "User full name.")
	private String fullName;

	@Getter
	@Setter
	@ApiModelProperty(value = "User email.")
	private String email;

	@Getter
	@Setter
	@ApiModelProperty(value = "User status.")
	private StatusType status;

	public UserModel(final User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.fullName = user.getFirstName() + " " + user.getLastName();
		this.email = user.getEmail();
		this.status = user.getStatus();
	}

}
