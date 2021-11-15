package com.bootcamp.project.eCommerce.service.services;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.SellerSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AddressSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UpdateSellerProfileSaveCO;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import org.springframework.web.multipart.MultipartFile;

public interface SellerService {

    public ResponseHandler<UserDTO> addSeller(SellerSaveCO sellerSaveCO, MultipartFile image) throws Exception;

    public ResponseHandler<UserDTO> getSeller(String token);

    public ResponseHandler updateProfile(String token, UpdateSellerProfileSaveCO updateSellerProfileSaveCO);

    public ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO);

    public ResponseHandler updateAddress(String token, AddressSaveCO addressSaveCO);

    ResponseHandler getAllCategory() throws GlobalException;
}
