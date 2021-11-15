package com.bootcamp.project.eCommerce.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.bootcamp.project.eCommerce.constants.TokenType;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import com.bootcamp.project.eCommerce.security.tokenPOJO.AuthorizationToken;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenUtil implements Serializable {

    static final long serialVersionUID = -2550185165626007488L;

    long JWT_TOKEN_VALIDITY = 60 * 60 * 24;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getUsernameFromClaims(Claims claims) {
        return claims.getSubject();
    }

    public String getTokenTypeFromClaims(Claims claims) {
        return claims.get("token_type", String.class);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails, TokenType tokenType) {
        JWT_TOKEN_VALIDITY = tokenType.getValidity();
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", tokenType.getKeyName());

        if (userDetails == null) {
            return doGenerateToken(claims);
        }
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    private String doGenerateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        User user = userRepository.findByEmail(username);
        AuthorizationToken authorizationToken = convertTokenToAuthTokenObject(token);

        if (!authorizationToken.equals(user.getAuthorizationToken())) {
            return false;
        }
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public AuthorizationToken convertTokenToAuthTokenObject(String token) {

        AuthorizationToken authorizationToken = new AuthorizationToken();
        authorizationToken.setToken(token);

        Claims claims = getAllClaimsFromToken(token);
        String tokenType = getTokenTypeFromClaims(claims);
        authorizationToken.setTokenType(tokenType);

        authorizationToken.setExpirationTime(getExpirationDateFromToken(token));
        authorizationToken.setUserName(getUsernameFromToken(token));
        authorizationToken.setClientSecret(secret);

        return authorizationToken;
    }
}