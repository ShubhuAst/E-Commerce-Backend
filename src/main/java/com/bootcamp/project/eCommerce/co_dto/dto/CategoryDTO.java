package com.bootcamp.project.eCommerce.co_dto.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDTO {

    Long id;

    String name;

    ParentCategoryDTO parentCategory;

    List<ChildCategoryDTO> childCategories;

    List<FieldValueDTO> fieldAndValues;

}
