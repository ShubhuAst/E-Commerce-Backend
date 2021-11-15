package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

}
