package com.bootcamp.project.eCommerce.co_dto.saveCO;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariationUpdateSaveCO {

    @NotNull(message = "Product Variation Id Can't be Null")
    Long id;

    @Min(value = 0)
    Integer quantityAvailable;

    @Min(value = 0)
    Integer price;

    String metadata;

    MultipartFile primaryImage;

    List<MultipartFile> secondaryImage;

    Boolean isActive;
}
