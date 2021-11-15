package com.bootcamp.project.eCommerce.co_dto.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationDTO {

    Long id;

    SupportingProductDTO product;

    Integer quantityAvailable;

    Integer price;

    String metadata;

    String primaryImage;

    List<String> secondaryImage;

    Boolean isActive;

//    OrderProduct orderProduct;

//    List<Cart> carts;
}
