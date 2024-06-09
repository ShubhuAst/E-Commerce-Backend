package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByEmail(String email);

    Seller findByGst(String gst);

    Seller findByCompanyNameIgnoreCase(String companyName);
}
