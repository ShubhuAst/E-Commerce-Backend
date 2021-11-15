package com.bootcamp.project.eCommerce.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum FileFor {

    USER_FILE("/user/","/images/defaults/product.jpeg"),
    PRODUCT_FILE("/product/","/images/defaults/product.jpeg"),
    PRODUCT_VARIATION_FILE("/productVariation/","/images/defaults/product.jpeg");

    private String path;
    private String defaultImagePath;


}