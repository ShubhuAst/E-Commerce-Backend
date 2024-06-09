package com.bootcamp.project.eCommerce.co_dto.saveCO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariationSaveCO {

    @NotNull(message = "Product Id Can't be Null")
    Long productId;

    @NotNull(message = "Quantity Available Can't be Null")
    @Min(value = 0)
    Integer quantityAvailable;

    @NotNull(message = "Price Can't be Null")
    @Min(value = 0)
    Integer price;

    @NotNull(message = "Metadata Can't be Null")
    String metadata;

    @NotNull(message = "Primary Image Can't be Null")
    MultipartFile primaryImage;

    List<MultipartFile> secondaryImage;

}
