package com.bootcamp.project.eCommerce.service.services.admin;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.saveCO.MetadataFieldValueSaveCO;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;

import java.util.Map;

public interface CategoryService {

    ResponseHandler addCategory(String name, Long parentID);

    ResponseHandler getCategory(Long id) throws GlobalException;

    ResponseHandler getAllCategory(Map<String, String> filter) throws GlobalException;

    ResponseHandler updateCategory(Long id, String name);

    ResponseHandler addMetadataField(String name);

    ResponseHandler getAllMetadataField(Map<String, String> filter);

    ResponseHandler addUpdateMetadataFieldValue(MetadataFieldValueSaveCO metadataFieldValueSaveCO, String requestType);

}
