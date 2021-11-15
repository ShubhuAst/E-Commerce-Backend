package com.bootcamp.project.eCommerce.service.services;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AdminSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface AdminService {

    ResponseHandler<UserDTO> addAdmin(AdminSaveCO adminSaveCO, MultipartFile image) throws IOException, GlobalException;

    ResponseHandler getAllCustomers(String token, Map<String, String> filter);

    ResponseHandler getAllSellers(String token, Map<String, String> filter);

    ResponseHandler activateUser(String token, Long id, String userType);

    ResponseHandler deActivateUser(String token, Long id, String userType);

    ResponseHandler getProduct(Long id);

    ResponseHandler getAllProduct(ProductAdminFilter productAdminFilter);

    ResponseHandler deactivateProduct(Long id);

    ResponseHandler activateProduct(Long id);
}
