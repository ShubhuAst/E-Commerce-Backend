package com.bootcamp.project.eCommerce.service.services.admin;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UserSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.UserFilterSaveCO;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {

    ResponseHandler<UserDTO> addAdmin(UserSaveCO adminSaveCO, MultipartFile image) throws IOException, GlobalException;

    ResponseHandler getAllCustomers(UserFilterSaveCO userFilterSaveCO);

    ResponseHandler getAllSellers(UserFilterSaveCO userFilterSaveCO);

    ResponseHandler activateUser(Long id, String userType);

    ResponseHandler deActivateUser(Long id, String userType);

    ResponseHandler getProduct(Long id);

    ResponseHandler getAllProduct(ProductAdminFilter productAdminFilter);

    ResponseHandler deactivateProduct(Long id);

    ResponseHandler activateProduct(Long id);
}
