package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ReSendTokenSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AddressSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UpdateCustomerProfileSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.CustomerProductFilter;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.CustomerSimilarProductFilter;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import com.bootcamp.project.eCommerce.service.services.CustomerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    final String HEADER_AUTHORIZATION = "Authorization";

    @PutMapping("/activate")
    public ResponseEntity<ResponseHandler> activateUser(@RequestHeader(HEADER_AUTHORIZATION) String token) throws Exception {

        ResponseHandler responseHandler = customerService.activateCustomer(token);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("activate/link")
    public ResponseEntity<ResponseHandler> reSendToken(@Valid @RequestBody ReSendTokenSaveCO reSendTokenSaveCO) throws Exception {

        ResponseHandler responseHandler = customerService.reSendToken(reSendTokenSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @GetMapping
    public ResponseEntity<ResponseHandler<UserDTO>> getCustomer(@RequestHeader(HEADER_AUTHORIZATION) String token) {

        ResponseHandler<UserDTO> responseHandler = customerService.getCustomer(token);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/address/all")
    public ResponseEntity<ResponseHandler> getAddresses(@RequestHeader(HEADER_AUTHORIZATION) String token) {

        ResponseHandler<UserDTO> responseHandler = customerService.getAddresses(token);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping
    public ResponseEntity<ResponseHandler> updateProfile(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody UpdateCustomerProfileSaveCO updateCustomerProfileSaveCO) {

        ResponseHandler responseHandler = customerService.updateProfile(token, updateCustomerProfileSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/reset/password")
    public ResponseEntity<ResponseHandler> resetPassword(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody ResetPasswordSaveCO resetPasswordSaveCO) {

        ResponseHandler responseHandler = customerService.resetPassword(token, resetPasswordSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/address")
    public ResponseEntity<ResponseHandler> addAddress(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                      @Valid @RequestBody Address address) {

        ResponseHandler responseHandler = customerService.addAddress(token, address);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @DeleteMapping("/address")
    public ResponseEntity<ResponseHandler> deleteAddress(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @RequestParam Long id) {

        ResponseHandler responseHandler = customerService.deleteAddress(token, id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/address")
    public ResponseEntity<ResponseHandler> updateAddress(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody AddressSaveCO addressSaveCO) {

        ResponseHandler responseHandler = customerService.updateAddress(token, addressSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/category/all")
    public ResponseEntity<ResponseHandler> getAllCategory(@RequestParam(required = false) Long categoryId) throws GlobalException {

        ResponseHandler responseHandler = customerService.getAllCategory(categoryId);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/category/filter")
    public ResponseEntity<ResponseHandler> getFilteredCategory(@RequestParam Long categoryId) throws GlobalException {

        ResponseHandler responseHandler = customerService.getFilteredCategory(categoryId);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/product")
    public ResponseEntity<ResponseHandler> getProduct(@RequestParam Long id){

        ResponseHandler responseHandler = customerService.getProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/product/all")
    public ResponseEntity<ResponseHandler> getAllProduct(@RequestBody CustomerProductFilter customerProductFilter){

        ResponseHandler responseHandler = customerService.getAllProduct(customerProductFilter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/product/similar")
    public ResponseEntity<ResponseHandler> getSimilarProduct(@RequestBody CustomerSimilarProductFilter customerSimilarProductFilter){

        ResponseHandler responseHandler = customerService.getSimilarProduct(customerSimilarProductFilter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}