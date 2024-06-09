package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.*;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.service.services.Product_SellerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/seller/product")
public class Product_SellerController {

    final Product_SellerService product_sellerService;

    @PostMapping
    public ResponseEntity<ResponseHandler> addProduct(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                      @Valid @RequestBody ProductSaveCO productSaveCO) {

        ResponseHandler responseHandler = product_sellerService.addProduct(token, productSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/variation")
    public ResponseEntity<ResponseHandler> addProductVariation(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                               @Valid @ModelAttribute ProductVariationSaveCO productVariationSaveCO) throws IOException {

        ResponseHandler responseHandler = product_sellerService.addProductVariation(token, productVariationSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping
    public ResponseEntity<ResponseHandler> getProduct(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                      @RequestParam Long id) {

        ResponseHandler responseHandler = product_sellerService.getProduct(token, id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/variation")
    public ResponseEntity<ResponseHandler> getProductVariation(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                               @RequestParam Long id) {

        ResponseHandler responseHandler = product_sellerService.getProductVariation(token, id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseHandler> getAllProduct(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @RequestBody(required = false) FilterSaveCO filterSaveCO) {

        ResponseHandler responseHandler = product_sellerService.getAllProduct(token, filterSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/variation/all")
    public ResponseEntity<ResponseHandler> getAllProductVariations(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                                   @RequestParam Long productId,
                                                                   @RequestBody(required = false) FilterSaveCO filterSaveCO) {

        ResponseHandler responseHandler = product_sellerService.getAllProductVariation(token, productId, filterSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @DeleteMapping
    public ResponseEntity<ResponseHandler> deleteProduct(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @RequestParam Long id) {

        ResponseHandler responseHandler = product_sellerService.deleteProduct(token, id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping
    public ResponseEntity<ResponseHandler> updateProduct(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody ProductUpdateSaveCO productUpdateSaveCO) {

        ResponseHandler responseHandler = product_sellerService.updateProduct(token, productUpdateSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping("/variation")
    public ResponseEntity<ResponseHandler> updateProductVariation(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                                  @Valid @ModelAttribute VariationUpdateSaveCO variationUpdateSaveCOSaveCO) throws IOException {

        ResponseHandler responseHandler = product_sellerService.updateProductVariation(token, variationUpdateSaveCOSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

}
