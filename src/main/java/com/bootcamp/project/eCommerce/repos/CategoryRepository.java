package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    List<Category> findAllByParentCategory(Category category);
}
