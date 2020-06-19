package com.scrumpoker.service;

import org.springframework.http.ResponseEntity;

import com.scrumpoker.request.UserAuthenticationRequest;
import com.scrumpoker.response.JwtResponse;

public interface AuthenticationService {

	ResponseEntity<JwtResponse> authenticate(final UserAuthenticationRequest userAuthentication);
}
