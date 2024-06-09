package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.LoginSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ReSendTokenSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.TokenType;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import com.bootcamp.project.eCommerce.security.JWTService;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.services.LoginLogoutService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginLogoutServiceImpl implements LoginLogoutService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final ModelMapper modelMapper;
    final EmailSenderService emailSenderService;
    final JWTService jwtService;

    @Override
    public ResponseHandler<UserDTO> userLogin(LoginSaveCO loginSaveCO) {

        User user = userRepository.findByEmail(loginSaveCO.getEmail());
        if (user == null) {
            return new ResponseHandler<>(AppResponse.USER_NOT_FOUND);
        }

        if (user.getInvalidAttemptCount() == 3) {
            user.setIsLocked(true);
            user.setInvalidAttemptCount(0);
            userRepository.save(user);

            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Your Account Gets Locked Due to Too Many Login Attempts",
                    "Activation Token For Your Account");

            return new ResponseHandler<>(AppResponse.ACCOUNT_LOCKED);
        }

        if (!passwordEncoder.matches(loginSaveCO.getPassword(), user.getPasswordHash())) {
            int attempt = user.getInvalidAttemptCount() + 1;
            user.setInvalidAttemptCount(attempt);
            userRepository.save(user);
            return ResponseHandler.<UserDTO>builder().msg("Invalid Password," + (3 - attempt) + " Attempt Left")
                    .statusCode(HttpStatus.FORBIDDEN.value()).build();
        }
        String token = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        user.setAuthorizationToken(token);
        user.setInvalidAttemptCount(0);
        userRepository.save(user);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setToken(token);

        return new ResponseHandler<>(userDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler userLogout(String token) {

        String jwtToken = token.substring(7);
        String userEmail = jwtService.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        user.setAuthorizationToken(null);
        userRepository.save(user);

        return new ResponseHandler(AppResponse.LOGOUT);
    }

    @Override
    public ResponseHandler sendPasswordResetToken(ReSendTokenSaveCO reSendTokenSaveCO) throws Exception {

        User user = userRepository.findByEmail(reSendTokenSaveCO.getEmail());

        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        String token = jwtService.generateToken(user, TokenType.RESET_PASSWORD_TOKEN);
        user.setAuthorizationToken(token);
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                token,
                "Reset Password Link");

        return new ResponseHandler(AppResponse.SEND_RESET_PASSWORD_LINK);
    }

    @Override
    public ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO) {

        String jwtToken = token.substring(7);
        String userEmail = jwtService.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        if (!resetPasswordSaveCO.getPassword().equals(resetPasswordSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(resetPasswordSaveCO.getPassword(), user.getPasswordHash())) {
            return new ResponseHandler<>(AppResponse.PASSWORD_CANNOT_BE_SAME_AS_PREVIOUS);
        }
        user.setPasswordHash(passwordEncoder.encode(resetPasswordSaveCO.getPassword()));
        user.setAuthorizationToken(null);
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Your Password is Updated Successfully",
                "Password Updated");

        return new ResponseHandler(AppResponse.PASSWORD_UPDATED);
    }

}
