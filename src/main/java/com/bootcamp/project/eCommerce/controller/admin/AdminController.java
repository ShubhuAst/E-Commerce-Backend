package com.bootcamp.project.eCommerce.controller.admin;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.UserFilterSaveCO;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.service.services.admin.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/admin")
@RequiredArgsConstructor
public class AdminController {

    final AdminService adminService;

    @GetMapping("/customer/all")
    public ResponseEntity<ResponseHandler> getAllCustomers(@RequestParam(required = false) UserFilterSaveCO userFilterSaveCO) {

        ResponseHandler responseHandler = adminService.getAllCustomers(userFilterSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @GetMapping("/seller/all")
    public ResponseEntity<ResponseHandler> getAllSellers(@RequestParam(required = false) UserFilterSaveCO userFilterSaveCO) {

        ResponseHandler responseHandler = adminService.getAllSellers(userFilterSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/customer/activate")
    public ResponseEntity<ResponseHandler> activateCustomer(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateUser(id, AppConstants.ROLE_CUSTOMER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/customer/deactivate")
    public ResponseEntity<ResponseHandler> deActivateCustomer(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deActivateUser(id, AppConstants.ROLE_CUSTOMER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/seller/activate")
    public ResponseEntity<ResponseHandler> activateSeller(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.activateUser(id, AppConstants.ROLE_SELLER);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));

    }

    @PatchMapping("/seller/deactivate")
    public ResponseEntity<ResponseHandler> deActivateSeller(@RequestParam Long id) {

        ResponseHandler responseHandler = adminService.deActivateUser(id, AppConstants.ROLE_SELLER);
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

