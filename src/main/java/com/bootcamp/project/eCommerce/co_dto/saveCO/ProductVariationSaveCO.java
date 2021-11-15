package com.bootcamp.project.eCommerce.co_dto.saveCO;

import com.bootcamp.project.eCommerce.pojos.orderFlow.Cart;
import com.bootcamp.project.eCommerce.pojos.orderFlow.OrderProduct;
import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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
