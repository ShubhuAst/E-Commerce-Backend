package com.bootcamp.project.eCommerce.co_dto.saveCO.filters;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerProductFilter extends PageFilterSaveCO {

    @NotNull(message = "Category Id Can't be Null")
    Long categoryId;

    String name;

    String brand;
}
