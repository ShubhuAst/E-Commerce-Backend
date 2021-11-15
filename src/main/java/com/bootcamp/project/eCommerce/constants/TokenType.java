package com.bootcamp.project.eCommerce.constants;

public enum TokenType {

    ACCESS_TOKEN(24 * 60 * 60, "access_token"),
    REFRESH_TOKEN(24 * 60 * 60, "refresh_token"),
    ACTIVATION_TOKEN(3 * 60 * 60, "activation_token"),
    RESET_PASSWORD_TOKEN(15 * 60, "reset_password_token");

    private long validity;
    private String keyName;


    TokenType(long validity, String keyName) {
        this.validity = validity;
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }

    public long getValidity() {
        return validity;
    }
}
