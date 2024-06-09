package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
