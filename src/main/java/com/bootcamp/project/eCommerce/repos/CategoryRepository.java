package com.bootcamp.project.eCommerce.repos;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    List<Category> findAllByParentCategory(Category category);
}
