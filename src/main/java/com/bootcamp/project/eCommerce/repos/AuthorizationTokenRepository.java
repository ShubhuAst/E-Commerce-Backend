package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.security.tokenPOJO.AuthorizationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationTokenRepository extends JpaRepository<AuthorizationToken, Long> {

    void deleteByToken(String token);

}
