package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.SellerSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UserSaveCO;
import com.bootcamp.project.eCommerce.service.services.AdminService;
import com.bootcamp.project.eCommerce.service.services.CustomerService;
import com.bootcamp.project.eCommerce.service.services.SellerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/register")
public class RegisterUserController {

    final CustomerService customerService;
    final SellerService sellerService;
    final AdminService adminService;

    @PostMapping("/customer")
    public ResponseEntity<ResponseHandler<UserDTO>> addCustomer(@Valid @RequestBody UserSaveCO customerSaveCO,
                                                                @RequestParam(required = false) MultipartFile image) throws Exception {

        ResponseHandler<UserDTO> responseHandler = customerService.addCustomer(customerSaveCO, image);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/seller")
    public ResponseEntity<ResponseHandler<UserDTO>> addSeller(@Valid @RequestBody SellerSaveCO sellerSaveCO,
                                                              @RequestParam(required = false) MultipartFile image) throws Exception {

        ResponseHandler<UserDTO> responseHandler = sellerService.addSeller(sellerSaveCO, image);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/admin")
    public ResponseEntity<ResponseHandler<UserDTO>> addAdmin(@Valid @RequestBody UserSaveCO adminSaveCO,
                                                             @RequestParam(required = false) MultipartFile image) throws Exception {

        ResponseHandler<UserDTO> responseHandler = adminService.addAdmin(adminSaveCO, image);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}
