package com.bootcamp.project.eCommerce.co_dto.saveCO.filters;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProductFilter {

    @NotNull(message = "Category Id Can't be Null")
    Long categoryId;

    Integer max;

    Integer offset;

    String sort;

    String order;

    String name;

    String brand;
}
