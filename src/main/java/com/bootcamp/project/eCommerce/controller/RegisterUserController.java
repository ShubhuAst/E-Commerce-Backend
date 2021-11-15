package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AdminSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.CustomerSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.SellerSaveCO;
import com.bootcamp.project.eCommerce.service.services.AdminService;
import com.bootcamp.project.eCommerce.service.services.CustomerService;
import com.bootcamp.project.eCommerce.service.services.SellerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/register")
public class RegisterUserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    SellerService sellerService;

    @Autowired
    AdminService adminService;

    @PostMapping("/customer")
    public ResponseEntity<ResponseHandler<UserDTO>> addCustomer(@Valid @RequestBody CustomerSaveCO customerSaveCO,
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
    public ResponseEntity<ResponseHandler<UserDTO>> addAdmin(@Valid @RequestBody AdminSaveCO adminSaveCO,
                                                             @RequestParam(required = false) MultipartFile image) throws Exception {

        ResponseHandler<UserDTO> responseHandler = adminService.addAdmin(adminSaveCO, image);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}
