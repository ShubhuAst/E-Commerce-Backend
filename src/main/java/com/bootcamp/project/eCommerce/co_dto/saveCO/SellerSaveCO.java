package com.bootcamp.project.eCommerce.co_dto.saveCO;

import jakarta.persistence.Column;
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
public class SellerSaveCO extends UserSaveCO {

    @NotNull
    @Column(nullable = false)
    String gst;

    @NotNull
    @Column(nullable = false)
    String companyName;

}
