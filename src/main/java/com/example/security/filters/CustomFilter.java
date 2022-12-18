package com.example.security.filters;

import com.example.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;

	private final String[] AUTH_WHITELIST = { "/token", "/register" };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// If we are on the WHITELIST we just go forward the filterChain
		for (String path : AUTH_WHITELIST) {
			if (request.getServletPath().startsWith(path)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String username;
		String token;
		if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
			response.sendError(400, "NO TOKEN");
			return;
		}
		else {
			token = requestTokenHeader.substring(7);
			try {
				username = jwtUtils.getSubject(token);
			}
			catch (Exception e) {
				response.sendError(401, "Unable to get token");
				return;
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if (jwtUtils.isValidToken(token)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, null);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		else {
			response.sendError(403, "Invalid Token");
			return;
		}
		filterChain.doFilter(request, response);
	}

}
