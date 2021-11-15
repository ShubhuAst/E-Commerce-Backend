package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataFieldValue;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataFieldValueID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMetadataFieldValueRepository extends JpaRepository<CategoryMetadataFieldValue, CategoryMetadataFieldValueID> {

    @Query(nativeQuery = true, value = "select * from category_metadata_field_value where category_id = :categoryId")
    List<CategoryMetadataFieldValue> findByCategoryId(@Param("categoryId") Long categoryId);
}
