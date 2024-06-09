package com.bootcamp.project.eCommerce.security;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.TokenType;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomRequestFilter extends OncePerRequestFilter {

    final UserDetailsService userDetailsService;
    final JWTService jwtService;
    final UserRepository userRepository;
    final EmailSenderService emailSenderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtService.getUsernameFromToken(jwtToken);
                    setUpAuthentication(username, jwtToken, request, response);
                } catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token");
                    setResponse(AppResponse.UNABLE_TO_GET_TOKEN, null, response);
                } catch (ExpiredJwtException e) {
                    logger.warn("JWT Token has expired");
                    handleExpiredToken(e.getClaims(), response);
                }
            } else {
                logger.warn("JWT Token does not begin with Bearer String");
                setResponse(AppResponse.INVALID_TOKEN, null, response);
            }
        } else {
            logger.warn("Token is Null");
        }
        filterChain.doFilter(request, response);
    }

    private void setUpAuthentication(String username, String jwtToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (userDetails == null) {
                setResponse(AppResponse.USER_NOT_FOUND, null, response);
                return;
            }
            if (!userDetails.isEnabled()) {
                setResponse(AppResponse.ACCOUNT_DISABLE, null, response);
                return;
            }
            if (!userDetails.isAccountNonLocked()) {
                setResponse(AppResponse.ACCOUNT_LOCKED, null, response);
            }
            if (!userDetails.isCredentialsNonExpired()) {
                setResponse(AppResponse.ACCOUNT_PASSWORD_EXPIRED, null, response);
            }

            if (jwtService.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                logger.warn("INVALID ACCESS TOKEN");
                setResponse(AppResponse.INVALID_TOKEN, null, response);
            }
        } else {
            logger.warn("Username is Null");
        }
    }

    private void handleExpiredToken(Claims claims, HttpServletResponse response) throws IOException {

        String tokenType = jwtService.getTokenTypeFromClaims(claims);

        if (TokenType.ACTIVATION_TOKEN.getKeyName().equals(tokenType) ||
                TokenType.ACCESS_TOKEN.getKeyName().equals(tokenType) ||
                TokenType.REFRESH_TOKEN.getKeyName().equals(tokenType)) {

            final User user = userRepository.findByEmail(claims.getSubject());
            if (user == null) {
                setResponse(AppResponse.TOKEN_EXPIRED, null, response);
                return;
            }

            String newToken = null;
            if (TokenType.ACTIVATION_TOKEN.getKeyName().equals(tokenType)) {
                newToken = jwtService.generateToken(user, TokenType.ACTIVATION_TOKEN);
            } else {
                newToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
            }

            user.setAuthorizationToken(newToken);
            userRepository.save(user);

            if (TokenType.ACTIVATION_TOKEN.getKeyName().equals(tokenType)) {
                emailSenderService.sendSimpleEmail(user.getEmail(),
                        newToken,
                        "Resend: Activation Token For Your Account");
                setResponse(AppResponse.ACTIVATION_TOKEN_EXPIRED, null, response);
            } else {
                setResponse(AppResponse.ISSUE_NEW_ACCESS_TOKEN, newToken, response);
            }
            return;
        }
        setResponse(AppResponse.TOKEN_EXPIRED, null, response);
    }

    private void setResponse(AppResponse appResponse, Object data, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(appResponse.getStatus().value());

        ResponseHandler responseHandler = null;
        if (data != null) {
            responseHandler = new ResponseHandler(data, appResponse);
        } else {
            responseHandler = new ResponseHandler(appResponse);
        }

        String responseJSON = new Gson().toJson(responseHandler);
        response.getOutputStream().print(responseJSON);
        response.flushBuffer();
    }
}
