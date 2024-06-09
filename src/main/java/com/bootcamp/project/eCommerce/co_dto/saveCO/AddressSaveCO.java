package com.bootcamp.project.eCommerce.co_dto.saveCO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class AddressSaveCO {

    @NotNull(message = "id Can't be Null")
    Long id;

    String city;

    String state;

    String country;

    String addressLine;

    @Min(value = 100000, message = "Invalid ZIP Code")
    @Max(value = 999999, message = "Invalid ZIP Code")
    Integer zipCode;

    String label;
}
