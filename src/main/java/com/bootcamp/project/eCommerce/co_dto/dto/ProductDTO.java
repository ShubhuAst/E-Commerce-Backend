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
public class ProductDTO {

    Long id;

    String name;

    String description;

    String brand;

    ProductCategoryDTO productCategory;

    Boolean isCancellable;

    Boolean isReturnable;

    Boolean isActive;

    Boolean isDeleted;

    List<SupportingProductVariationDTO> productVariations;

//    List<ProductReview> productReviews;
}
