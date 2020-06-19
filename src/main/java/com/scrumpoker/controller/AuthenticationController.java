package com.scrumpoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scrumpoker.request.UserAuthenticationRequest;
import com.scrumpoker.response.JwtResponse;
import com.scrumpoker.service.AuthenticationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/signin")
	@ApiOperation(value = "Get credentials to sign in.")
	public ResponseEntity<JwtResponse> signIn(@RequestBody final UserAuthenticationRequest userAuthentication) {
		return authenticationService.authenticate(userAuthentication);		
	}	
}
