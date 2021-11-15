package com.bootcamp.project.eCommerce.service.services;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.*;

import java.io.IOException;

public interface Product_SellerService {

    ResponseHandler addProduct(String token, ProductSaveCO productSaveCO);

    ResponseHandler getProduct(String token, Long id);

    ResponseHandler addProductVariation(String token, ProductVariationSaveCO productVariationSaveCO) throws IOException;

    ResponseHandler getProductVariation(String token, Long id);

    ResponseHandler getAllProduct(String token, FilterSaveCO filterSaveCO);

    ResponseHandler getAllProductVariation(String token, Long productId, FilterSaveCO filterSaveCO);

    ResponseHandler deleteProduct(String token, Long id);

    ResponseHandler updateProduct(String token, ProductUpdateSaveCO productUpdateSaveCO);

    ResponseHandler updateProductVariation(String token, VariationUpdateSaveCO variationUpdateSaveCOSaveCO) throws IOException;
}
