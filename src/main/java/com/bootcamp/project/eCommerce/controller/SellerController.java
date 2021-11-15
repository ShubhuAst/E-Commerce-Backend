package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AddressSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UpdateSellerProfileSaveCO;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.service.services.SellerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    final String HEADER_AUTHORIZATION = "Authorization";

    @GetMapping
    public ResponseEntity<ResponseHandler<UserDTO>> getSeller(@RequestHeader(HEADER_AUTHORIZATION) String token) {

        ResponseHandler<UserDTO> responseHandler = sellerService.getSeller(token);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping
    public ResponseEntity<ResponseHandler> updateProfile(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody UpdateSellerProfileSaveCO updateSellerProfileSaveCO) {

        ResponseHandler responseHandler = sellerService.updateProfile(token, updateSellerProfileSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/reset/password")
    public ResponseEntity<ResponseHandler> resetPassword(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody ResetPasswordSaveCO resetPasswordSaveCO) {

        ResponseHandler responseHandler = sellerService.resetPassword(token, resetPasswordSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/address")
    public ResponseEntity<ResponseHandler> updateAddress(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody AddressSaveCO addressSaveCO) {

        ResponseHandler responseHandler = sellerService.updateAddress(token, addressSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/category/all")
    public ResponseEntity<ResponseHandler> getAllCategory(@RequestHeader(HEADER_AUTHORIZATION) String token) throws GlobalException {

        ResponseHandler responseHandler = sellerService.getAllCategory();
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}
