package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.MetadataFieldValueSaveCO;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.service.services.Category_AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session/admin/category")
@RequiredArgsConstructor
public class Category_AdminController {

    final Category_AdminService categoryAdminService;

    @PostMapping
    public ResponseEntity addCategory(@RequestParam String name,
                                      @RequestParam(required = false) Long parentID) {

        ResponseHandler responseHandler = categoryAdminService.addCategory(name, parentID);
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping
    public ResponseEntity getCategory(@RequestParam Long id) throws GlobalException {

        ResponseHandler responseHandler = categoryAdminService.getCategory(id);
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/all")
    public ResponseEntity getAllCategory(@RequestParam(required = false) Map<String, String> filter) throws GlobalException {

        ResponseHandler responseHandler = categoryAdminService.getAllCategory(filter);
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping
    public ResponseEntity updateCategory(@RequestParam String name,
                                         @RequestParam Long id) {

        ResponseHandler responseHandler = categoryAdminService.updateCategory(id, name);
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/metadata/field")
    public ResponseEntity addMetadataField(@RequestParam String name) {

        ResponseHandler responseHandler = categoryAdminService.addMetadataField(name);
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @GetMapping("/metadata/field/all")
    public ResponseEntity getAllMetadataField(@RequestParam(required = false) Map<String, String> filter) {

        ResponseHandler responseHandler = categoryAdminService.getAllMetadataField(filter);
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/metadata/field/value")
    public ResponseEntity addMetadataFieldValue(@RequestBody MetadataFieldValueSaveCO metadataFieldValueSaveCO) {

        ResponseHandler responseHandler = categoryAdminService.addUpdateMetadataFieldValue(metadataFieldValueSaveCO, "POST");
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PutMapping("/metadata/field/value")
    public ResponseEntity updateMetadataFieldValues(@RequestBody MetadataFieldValueSaveCO metadataFieldValueSaveCO) {

        ResponseHandler responseHandler = categoryAdminService.addUpdateMetadataFieldValue(metadataFieldValueSaveCO, "PUT");
        return new ResponseEntity(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}
