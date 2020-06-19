package com.scrumpoker.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.scrumpoker.entity.User;
import com.scrumpoker.models.UserModel;
import com.scrumpoker.repository.UserRepository;
import com.scrumpoker.request.UserAuthenticationRequest;
import com.scrumpoker.response.JwtResponse;
import com.scrumpoker.service.AuthenticationService;
import com.scrumpoker.utils.JwtTokenUtil;
import com.scrumpoker.utils.Translator;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Override
	public ResponseEntity<JwtResponse> authenticate(final UserAuthenticationRequest userAuthentication) {
		authenticate(userAuthentication.getEmail(), userAuthentication.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthentication.getEmail());
		final String token = jwtTokenUtil.generateToken(userDetails);
		final Optional<User> user = userRepository.findByEmail(userAuthentication.getEmail());
		return ResponseEntity.ok(new JwtResponse(new UserModel(user.get()),token));
	}

	private void authenticate(final String username, final String password) {		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException(Translator.toLocale("UserDisabled"));
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(Translator.toLocale("InvalidCredentials"));
		}
	}
}
