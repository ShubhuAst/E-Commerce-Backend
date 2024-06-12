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
public class CustomerSimilarProductFilter extends PageFilterSaveCO {

    @NotNull(message = "Product Id Can't be Null")
    Long productId;

    String name;
}
