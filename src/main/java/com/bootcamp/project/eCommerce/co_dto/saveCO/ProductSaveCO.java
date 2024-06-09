package com.bootcamp.project.eCommerce.co_dto.saveCO;

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
public class ProductSaveCO {

    @NotNull(message = "Product name Can't be Null")
    String name;

    String description;

    @NotNull(message = "Category Id Can't be Null")
    Long categoryId;

    Boolean isCancellable;

    Boolean isReturnable;

    @NotNull(message = "Brand Can't be Null")
    String brand;
}
