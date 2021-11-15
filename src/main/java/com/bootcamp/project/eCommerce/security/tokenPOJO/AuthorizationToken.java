package com.bootcamp.project.eCommerce.security.tokenPOJO;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.GrantedAuthorityImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorizationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String token;

    String tokenType;

    String userName;

    String clientSecret;

    @Temporal(TemporalType.TIMESTAMP)
    Date expirationTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizationToken)) return false;
        AuthorizationToken that = (AuthorizationToken) o;
        return getToken().equals(that.getToken()) && getTokenType().equals(that.getTokenType()) && getUserName().equals(that.getUserName()) && getClientSecret().equals(that.getClientSecret()) && getExpirationTime().equals(that.getExpirationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken(), getTokenType(), getUserName(), getClientSecret(), getExpirationTime());
    }
}
