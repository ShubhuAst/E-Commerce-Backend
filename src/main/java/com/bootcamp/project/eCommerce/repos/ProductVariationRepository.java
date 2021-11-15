package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {

    ProductVariation findByMetadataAndProduct(String metadata, Product product);

    List<ProductVariation> findByProduct(Product product);

    List<ProductVariation> findByProduct(Product product, Pageable pageable);
}
