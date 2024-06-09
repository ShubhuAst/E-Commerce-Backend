package com.bootcamp.project.eCommerce.pojos.userFlow.user;

import com.bootcamp.project.eCommerce.pojos.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends Auditable implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1201295790102223956L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    @Email(message = "Please provide a valid email address")
    String email;

    String firstName;

    String middleName;

    String lastName;

    @NotNull
    @Column(nullable = false)
    String passwordHash;

    @NotNull
    @Column(nullable = false)
    String contact;

    Boolean isDeleted = false;

    Boolean isActive = false;

    Boolean isExpired = false;

    Boolean isCredentialsExpired = false;

    Boolean isLocked = false;

    Integer invalidAttemptCount = 0;

    @Temporal(TemporalType.TIMESTAMP)
    Date passwordLastUpdated;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<GrantedAuthorityImpl> grantedAuthorities;

    String authorizationToken;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    List<Address> addresses;

    @Version
    Long version;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public void setPasswordHash(String password) {
        passwordLastUpdated = new Date();
        this.passwordHash = password;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    public Boolean doesContainsAddress(Address address) {
        return addresses.contains(address);
    }
}
