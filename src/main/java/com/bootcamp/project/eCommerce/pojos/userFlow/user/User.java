package com.bootcamp.project.eCommerce.pojos.userFlow.user;

import com.bootcamp.project.eCommerce.security.tokenPOJO.AuthorizationToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String email;

    String firstName;

    String middleName;

    String lastName;

    String password;

    Boolean isDeleted = false;

    Boolean isActive = false;

    Boolean isExpired = false;

    Boolean isCredentialsExpired = false;

    Boolean isLocked = false;

    Integer invalidAttemptCount = 0;

    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    Date lastUpdated;

    String createdBy;

    String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    Date password_last_updated;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    List<GrantedAuthorityImpl> grantedAuthorities;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "auth_token_id", referencedColumnName = "id")
    AuthorizationToken authorizationToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Address> address;

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

    @PrePersist
    protected void onCreate() {
        dateCreated = new Date();
        createdBy = email;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
        updatedBy = email;
    }

    public void setPassword(String password) {
        password_last_updated = new Date();
        this.password = password;
    }

    public void setAddress(List<Address> address) {
        address.forEach(addr -> addr.setUser(this));
        this.address = address;
    }
}
