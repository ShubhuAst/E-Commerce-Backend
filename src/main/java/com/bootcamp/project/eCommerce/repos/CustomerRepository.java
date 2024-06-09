package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

}
