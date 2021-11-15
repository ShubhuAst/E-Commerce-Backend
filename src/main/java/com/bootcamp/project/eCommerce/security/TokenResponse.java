package com.bootcamp.project.eCommerce.security;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse implements Serializable {

    static final long serialVersionUID = -8091879091924046844L;

    final String jwttoken;

    public TokenResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }


}