package com.userService.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.userService.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("Processing authorization request for URL: {}", request.getRequestURI());

        // Get the authorization header from the request
        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            // Extract the token after "Bearer "
            token = requestHeader.substring(7);

            try {
                // Extract the username from the token
                username = jwtService.extractUsername(token);

            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token due to an illegal argument", e);
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired", e);
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT Token - possibly tampered with", e);
            } catch (Exception e) {
                logger.error("An error occurred while processing the JWT token", e);
            }
        } else {
            logger.info("Authorization header is either missing or does not start with 'Bearer '");
        }

        // If a valid token is found, and there is no current authentication in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user details from the service
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtService.validateToken(token)) {
                logger.info("JWT Token is valid for user: {}", username);

                // Create an authentication token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Set the details in the security context
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication to the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication set in SecurityContext for user: {}", username);
            } else {
                logger.warn("JWT Token validation failed for user: {}", username);
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}
