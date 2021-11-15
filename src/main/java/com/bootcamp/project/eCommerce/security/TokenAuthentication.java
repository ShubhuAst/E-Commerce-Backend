package com.bootcamp.project.eCommerce.security;

import com.bootcamp.project.eCommerce.constants.TokenType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenAuthentication {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    UserDetailsService userDetailsService;

    public TokenResponse createAuthenticationToken(String username, String password, TokenType tokenType) throws Exception {

        if (!tokenType.equals(TokenType.ACTIVATION_TOKEN)) {
            authenticate(username, password);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        final String token = tokenUtil.generateToken(userDetails, tokenType);
        return new TokenResponse(token);
    }

    public TokenResponse createAuthenticationToken(TokenType tokenType) throws Exception {

        final String token = tokenUtil.generateToken(null, tokenType);
        return new TokenResponse(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}