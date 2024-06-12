package com.bootcamp.project.eCommerce.service.services.seller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ProductSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ProductUpdateSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ProductVariationSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.VariationUpdateSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.PageFilterSaveCO;

import java.io.IOException;

public interface ProductService {

    ResponseHandler addProduct(String token, ProductSaveCO productSaveCO);

    ResponseHandler getProduct(String token, Long id);

    ResponseHandler addProductVariation(String token, ProductVariationSaveCO productVariationSaveCO) throws IOException;

    ResponseHandler getProductVariation(String token, Long id);

    ResponseHandler getAllProduct(String token, PageFilterSaveCO pageFilterSaveCO);

    ResponseHandler getAllProductVariation(String token, Long productId, PageFilterSaveCO pageFilterSaveCO);

    ResponseHandler deleteProduct(String token, Long id);

    ResponseHandler updateProduct(String token, ProductUpdateSaveCO productUpdateSaveCO);

    ResponseHandler updateProductVariation(String token, VariationUpdateSaveCO variationUpdateSaveCOSaveCO) throws IOException;
}
