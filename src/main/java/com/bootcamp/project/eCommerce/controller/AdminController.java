package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.service.services.AdminService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    final String HEADER_AUTHORIZATION = "Authorization";
    final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    final static String ROLE_SELLER = "ROLE_SELLER";
    final static String ROLE_ADMIN = "ROLE_ADMIN";

    @GetMapping("/customer/all")
    public ResponseEntity<ResponseHandler> getAllCustomers(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                           @RequestParam(required = false) Map<String, String> filter) {

        ResponseHandler responseHandler = adminService.getAllCustomers(token, filter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @GetMapping("/seller/all")
    public ResponseEntity<ResponseHandler> getAllSellers(@RequestHeader(HEADER_AUTHORIZATION) String token,
                                                         @RequestParam(required = false) Map<String, String> filter) {

        ResponseHandler responseHandler = adminService.getAllSellers(token, filter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/customer/activate")
    public ResponseEntity<ResponseHandler> activateCustomer(@RequestHeader(HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateUser(token, id, ROLE_CUSTOMER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/customer/deactivate")
    public ResponseEntity<ResponseHandler> deActivateCustomer(@RequestHeader(HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deActivateUser(token, id, ROLE_CUSTOMER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/seller/activate")
    public ResponseEntity<ResponseHandler> activateSeller(@RequestHeader(HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateUser(token, id, ROLE_SELLER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/seller/deactivate")
    public ResponseEntity<ResponseHandler> deActivateSeller(@RequestHeader(HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deActivateUser(token, id, ROLE_SELLER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @GetMapping("/product")
    public ResponseEntity<ResponseHandler> getProduct(@RequestParam Long id){

        ResponseHandler responseHandler = adminService.getProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/product/all")
    public ResponseEntity<ResponseHandler> getAllProduct(@RequestBody(required = false) ProductAdminFilter productAdminFilter ){

        ResponseHandler responseHandler = adminService.getAllProduct(productAdminFilter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping("/product/deactivate")
    public ResponseEntity<ResponseHandler> deactivateProduct(@RequestParam Long id){

        ResponseHandler responseHandler = adminService.deactivateProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping("/product/activate")
    public ResponseEntity<ResponseHandler> activateProduct(@RequestParam Long id){

        ResponseHandler responseHandler = adminService.activateProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}

