package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByBrand(String brand);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByCategory(Category category, Pageable pageable);

    List<Product> findAllBySeller(Seller seller, Pageable pageable);

    List<Product> findAllBySeller(Seller seller);

    List<Product> findAllBySellerAndIsDeleted(Seller seller,Boolean isDeleted ,Pageable pageable);

    List<Product> findAllByCategoryAndBrand(Category category, String brand, Pageable pageable);

    List<Product> findAllByName(String name, Pageable pageable);
}
