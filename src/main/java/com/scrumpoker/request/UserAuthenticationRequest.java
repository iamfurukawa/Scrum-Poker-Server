package com.scrumpoker.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticationRequest {

	@ApiModelProperty(value = "User password.")
	private String password;

	@ApiModelProperty(value = "Email password.")
	private String email;
}
