package com.bootcamp.project.eCommerce.service.services;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.LoginSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ReSendTokenSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;

public interface LoginLogoutService {

    public ResponseHandler<UserDTO> userLogin(LoginSaveCO loginSaveCO) throws Exception;

    public ResponseHandler userLogout(String token);

    public ResponseHandler sendPasswordResetToken(ReSendTokenSaveCO reSendTokenSaveCO) throws Exception;


    public ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO);
}
