package com.bootcamp.project.eCommerce.controller.seller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AddressSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UpdateSellerProfileSaveCO;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.service.services.seller.SellerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/seller")
public class SellerController {

    final SellerService sellerService;

    @GetMapping
    public ResponseEntity<ResponseHandler<UserDTO>> getSeller(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token) {

        ResponseHandler<UserDTO> responseHandler = sellerService.getSeller(token);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping
    public ResponseEntity<ResponseHandler> updateProfile(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody UpdateSellerProfileSaveCO updateSellerProfileSaveCO) {

        ResponseHandler responseHandler = sellerService.updateProfile(token, updateSellerProfileSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/reset/password")
    public ResponseEntity<ResponseHandler> resetPassword(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody ResetPasswordSaveCO resetPasswordSaveCO) {

        ResponseHandler responseHandler = sellerService.resetPassword(token, resetPasswordSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/address")
    public ResponseEntity<ResponseHandler> updateAddress(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody AddressSaveCO addressSaveCO) {

        ResponseHandler responseHandler = sellerService.updateAddress(token, addressSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/category/all")
    public ResponseEntity<ResponseHandler> getAllCategory(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token) throws GlobalException {

        ResponseHandler responseHandler = sellerService.getAllCategory();
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}
