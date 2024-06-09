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
public class ProductUpdateSaveCO {

    @NotNull(message = "Product Id Can't be Null")
    Long id;

    String name;

    String description;

    Boolean isCancellable;

    Boolean isReturnable;

}
