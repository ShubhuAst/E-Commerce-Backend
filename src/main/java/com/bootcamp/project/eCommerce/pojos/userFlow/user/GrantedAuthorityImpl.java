package com.bootcamp.project.eCommerce.pojos.userFlow.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "role")
public class GrantedAuthorityImpl implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 6690834373789241583L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Authority Can't be Null")
    String authority;

    @Version
    Long version;

    @Override
    public String getAuthority() {
        return authority;
    }
}
