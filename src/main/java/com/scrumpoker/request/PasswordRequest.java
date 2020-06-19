package com.scrumpoker.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {
	
	@ApiModelProperty(value = "New password.")
	private String password;
}
