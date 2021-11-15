package com.bootcamp.project.eCommerce.service.services;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.*;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.CustomerProductFilter;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.CustomerSimilarProductFilter;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    ResponseHandler<UserDTO> addCustomer(CustomerSaveCO customerSaveCO, MultipartFile image) throws Exception;

    ResponseHandler activateCustomer(String token);

    ResponseHandler reSendToken(ReSendTokenSaveCO reSendTokenSaveCO) throws Exception;

    ResponseHandler<UserDTO> getCustomer(String token);

    ResponseHandler getAddresses(String token);

    ResponseHandler updateProfile(String token, UpdateCustomerProfileSaveCO updateCustomerProfileSaveCO);

    ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO);

    ResponseHandler addAddress(String token, Address address);

    ResponseHandler deleteAddress(String token, Long id);

    ResponseHandler updateAddress(String token, AddressSaveCO addressSaveCO);

    ResponseHandler getAllCategory(Long categoryId) throws GlobalException;

    ResponseHandler getFilteredCategory(Long categoryId) throws GlobalException;

    ResponseHandler getProduct(Long id);

    ResponseHandler getAllProduct(CustomerProductFilter customerProductFilter);

    ResponseHandler getSimilarProduct(CustomerSimilarProductFilter customerSimilarProductFilter);

}
