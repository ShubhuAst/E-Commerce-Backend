package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetadataField, Long> {

    CategoryMetadataField findByName(String name);
}
