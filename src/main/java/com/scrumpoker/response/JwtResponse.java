package com.scrumpoker.response;

import com.scrumpoker.models.UserModel;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtResponse {

	@ApiModelProperty(value = "User information.")
	private UserModel user;

	@ApiModelProperty(value = "A token JWT.")
	private final String token;
}