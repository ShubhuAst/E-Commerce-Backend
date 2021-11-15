package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.TokenType;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.LoginSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ReSendTokenSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.AuthorizationTokenRepository;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import com.bootcamp.project.eCommerce.security.TokenAuthentication;
import com.bootcamp.project.eCommerce.security.TokenResponse;
import com.bootcamp.project.eCommerce.security.TokenUtil;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.services.LoginLogoutService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginLogoutServiceImpl implements LoginLogoutService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenAuthentication tokenAuthentication;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    AuthorizationTokenRepository tokenRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public ResponseHandler<UserDTO> userLogin(LoginSaveCO loginSaveCO) throws Exception {

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

        if (!passwordEncoder.matches(loginSaveCO.getPassword(), user.getPassword())) {
            int attempt = user.getInvalidAttemptCount() + 1;
            user.setInvalidAttemptCount(attempt);
            userRepository.save(user);
            return ResponseHandler.<UserDTO>builder().msg("Invalid Password," + (3 - attempt) + " Attempt Left")
                    .statusCode(HttpStatus.FORBIDDEN.value()).build();
        }

//        String oldToken = user.getAuthorizationToken().getToken();
        TokenResponse tokenResponse = tokenAuthentication.createAuthenticationToken(loginSaveCO.getEmail(),
                loginSaveCO.getPassword(), TokenType.ACCESS_TOKEN);
        user.setAuthorizationToken(tokenUtil.convertTokenToAuthTokenObject(tokenResponse.getToken()));
        user.setInvalidAttemptCount(0);
        userRepository.save(user);
//        if (oldToken != null){
//            tokenRepository.deleteByToken(oldToken);
//        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setToken(tokenResponse.getToken());

        return new ResponseHandler<>(userDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler userLogout(String token) {

        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

//        if (user.getAuthorizationToken().getToken() != null){
//            tokenRepository.deleteByToken(user.getAuthorizationToken().getToken());
//        }
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

//        if (user.getAuthorizationToken().getToken() != null){
//            tokenRepository.deleteByToken(user.getAuthorizationToken().getToken());
//        }
        String token = tokenUtil.generateToken(user, TokenType.RESET_PASSWORD_TOKEN);
        user.setAuthorizationToken(tokenUtil.convertTokenToAuthTokenObject(token));
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                token,
                "Reset Password Link");

        return new ResponseHandler(AppResponse.SEND_RESET_PASSWORD_LINK);
    }

    @Override
    public ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO) {

        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        if (!resetPasswordSaveCO.getPassword().equals(resetPasswordSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(resetPasswordSaveCO.getPassword(), user.getPassword())) {
            return new ResponseHandler<>(AppResponse.PASSWORD_CANNOT_BE_SAME_AS_PREVIOUS);
        }

        user.setPassword(passwordEncoder.encode(resetPasswordSaveCO.getPassword()));
//        if (user.getAuthorizationToken().getToken() != null){
//            tokenRepository.deleteByToken(user.getAuthorizationToken().getToken());
//        }
        user.setAuthorizationToken(null);
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Your Password is Updated Successfully",
                "Password Updated");

        return new ResponseHandler(AppResponse.PASSWORD_UPDATED);
    }

}
