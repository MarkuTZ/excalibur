package com.example.filters;

import com.example.models.User;
import com.example.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userServiceUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String userService = null;

        if (requestTokenHeader != null) {
            //TODO: Add substring(7) or something like that. Token comes like this "BEARER ASDASDQWEASD"
            userService = requestTokenHeader;
            try {
                username = userServiceUtil.getSubject(userService);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get token");
            } catch (ExpiredJwtException e) {
                System.out.println("Token has expired");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userServiceUtil.getUser(username);
            if (userServiceUtil.isValidToken(userService, username)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
