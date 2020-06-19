package com.scrumpoker.middleware;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.scrumpoker.implementation.JwtUserDetailsService;
import com.scrumpoker.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;

		if (isTokenBearer(requestTokenHeader)) {
			jwtToken = requestTokenHeader.substring(7);
			
			try {
				username = jwtTokenUtil.getEmailFromToken(jwtToken);
			} catch (final IllegalArgumentException e) {
				log.error("Unable to get JWT Token");
			} catch (final ExpiredJwtException e) {
				log.error("JWT Token has expired");
			} catch (final SignatureException e) {
				log.error("Signature not match");
			}
		} else {
			log.warn("JWT Token does not begin with Bearer String");
		}

		if (isUnauthenticated(username)) {
			final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			if (isTokenValid(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}

	private boolean isTokenValid(String jwtToken, final UserDetails userDetails) {
		return Boolean.TRUE.equals(jwtTokenUtil.isValidToken(jwtToken, userDetails));
	}

	private boolean isUnauthenticated(final String username) {
		return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
	}

	private boolean isTokenBearer(final String requestTokenHeader) {
		return requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ");
	}
}