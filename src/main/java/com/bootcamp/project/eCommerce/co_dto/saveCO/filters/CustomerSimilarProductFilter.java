package com.bootcamp.project.eCommerce.co_dto.saveCO.filters;

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
public class CustomerSimilarProductFilter {

    @NotNull(message = "Product Id Can't be Null")
    Long productId;

    Integer max;

    Integer offset;

    String sort;

    String order;

    String name;
}
