package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
