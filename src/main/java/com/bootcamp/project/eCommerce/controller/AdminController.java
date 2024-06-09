package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.service.services.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/admin")
@RequiredArgsConstructor
public class AdminController {

    final AdminService adminService;

    @GetMapping("/customer/all")
    public ResponseEntity<ResponseHandler> getAllCustomers(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                           @RequestParam(required = false) Map<String, String> filter) {

        ResponseHandler responseHandler = adminService.getAllCustomers(token, filter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @GetMapping("/seller/all")
    public ResponseEntity<ResponseHandler> getAllSellers(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @RequestParam(required = false) Map<String, String> filter) {

        ResponseHandler responseHandler = adminService.getAllSellers(token, filter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/customer/activate")
    public ResponseEntity<ResponseHandler> activateCustomer(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateUser(token, id, AppConstants.ROLE_CUSTOMER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/customer/deactivate")
    public ResponseEntity<ResponseHandler> deActivateCustomer(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deActivateUser(token, id, AppConstants.ROLE_CUSTOMER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/seller/activate")
    public ResponseEntity<ResponseHandler> activateSeller(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateUser(token, id, AppConstants.ROLE_SELLER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/seller/deactivate")
    public ResponseEntity<ResponseHandler> deActivateSeller(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token, @RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deActivateUser(token, id, AppConstants.ROLE_SELLER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @GetMapping("/product")
    public ResponseEntity<ResponseHandler> getProduct(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.getProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/product/all")
    public ResponseEntity<ResponseHandler> getAllProduct(@RequestBody(required = false) ProductAdminFilter productAdminFilter) {

        ResponseHandler responseHandler = adminService.getAllProduct(productAdminFilter);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping("/product/deactivate")
    public ResponseEntity<ResponseHandler> deactivateProduct(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deactivateProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping("/product/activate")
    public ResponseEntity<ResponseHandler> activateProduct(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateProduct(id);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}

