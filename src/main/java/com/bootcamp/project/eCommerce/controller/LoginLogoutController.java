package com.bootcamp.project.eCommerce.controller;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.LoginSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ReSendTokenSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.service.services.LoginLogoutService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/session")
public class LoginLogoutController {

    final LoginLogoutService loginLogoutService;

    @PostMapping("/login")
    public ResponseEntity<ResponseHandler<UserDTO>> userLogin(@Valid @RequestBody LoginSaveCO loginSaveCO) throws Exception {

        ResponseHandler<UserDTO> responseHandler = loginLogoutService.userLogin(loginSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseHandler> userLogout(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token) throws Exception {

        ResponseHandler responseHandler = loginLogoutService.userLogout(token);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PostMapping("/reset/password/link")
    public ResponseEntity<ResponseHandler> sendResetPasswordLink(@Valid @RequestBody ReSendTokenSaveCO reSendTokenSaveCO) throws Exception {

        ResponseHandler responseHandler = loginLogoutService.sendPasswordResetToken(reSendTokenSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @PatchMapping("/reset/password")
    public ResponseEntity<ResponseHandler> resetPassword(@RequestHeader(AppConstants.HEADER_AUTHORIZATION) String token,
                                                         @Valid @RequestBody ResetPasswordSaveCO resetPasswordSaveCO) throws Exception {

        ResponseHandler responseHandler = loginLogoutService.resetPassword(token, resetPasswordSaveCO);
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }
}