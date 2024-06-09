package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.CategoryDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AddressSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.ResetPasswordSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.SellerSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UpdateSellerProfileSaveCO;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.constants.AppData;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.GrantedAuthorityImpl;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.*;
import com.bootcamp.project.eCommerce.security.JWTService;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.FileUploadService;
import com.bootcamp.project.eCommerce.service.services.SellerService;
import com.bootcamp.project.eCommerce.utils.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerServiceImpl implements SellerService {

    final UserRepository userRepository;
    final SellerRepository sellerRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    final EmailSenderService emailSenderService;
    final JWTService jwtService;
    final AddressRepository addressRepository;
    final CategoryRepository categoryRepository;
    final FileUploadService fileUploadService;
    final CategoryMetadataFieldRepository metadataFieldRepository;
    final CategoryMetadataFieldValueRepository metadataFieldValueRepository;
    final Utils utils;

    @Override
    public ResponseHandler<UserDTO> addSeller(SellerSaveCO sellerSaveCO, MultipartFile image) throws Exception {

        if (!sellerSaveCO.getPassword().equals(sellerSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }
        if (AppData.MASTER_ADMIN.getData().equals(sellerSaveCO.getEmail())) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        User user = userRepository.findByEmail(sellerSaveCO.getEmail());
        if (user != null) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }
        Seller sellerFromDB = sellerRepository.findByGst(sellerSaveCO.getGst());
        if (sellerFromDB != null) {
            return new ResponseHandler<>(AppResponse.SELLER_WITH_GST_ALREADY_EXIST);
        }
        sellerFromDB = sellerRepository.findByCompanyNameIgnoreCase(sellerSaveCO.getCompanyName());
        if (sellerFromDB != null) {
            return new ResponseHandler<>(AppResponse.SELLER_WITH_COMPANY_NAME_ALREADY_EXIST);
        }

        Seller seller = modelMapper.map(sellerSaveCO, Seller.class);

        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl();
        grantedAuthority.setAuthority(AppConstants.ROLE_SELLER);
        seller.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        seller.setAddresses(Arrays.asList(sellerSaveCO.getAddress()));

        seller.setPasswordHash(passwordEncoder.encode(seller.getPasswordHash()));
        userRepository.save(seller);

        User savedUser = userRepository.findByEmail(seller.getEmail());

        emailSenderService.sendSimpleEmail(seller.getEmail(),
                "Account Successfully Created\n\n Waiting for Admin to Approval.",
                "Account Created!");

        return new ResponseHandler<>(AppResponse.SELLER_ACCOUNT_CREATED);
    }

    @Override
    public ResponseHandler<UserDTO> getSeller(String token) {

        String jwtToken = token.substring(7);
        String username = jwtService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);

        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        UserDTO userDTO = modelMapper.map(seller, UserDTO.class);
        return new ResponseHandler<>(userDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler updateProfile(String token, UpdateSellerProfileSaveCO updateSellerProfileSaveCO) {

        String jwtToken = token.substring(7);
        String username = jwtService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);

        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        utils.copyNonNullProperties(updateSellerProfileSaveCO, seller);
        sellerRepository.save(seller);

        return new ResponseHandler(AppResponse.PROFILE_UPDATED);
    }

    @Override
    public ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO) {

        String jwtToken = token.substring(7);
        String userEmail = jwtService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(userEmail);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        if (!resetPasswordSaveCO.getPassword().equals(resetPasswordSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(resetPasswordSaveCO.getPassword(), seller.getPasswordHash())) {
            return new ResponseHandler<>(AppResponse.PASSWORD_CANNOT_BE_SAME_AS_PREVIOUS);
        }
        seller.setAuthorizationToken(null);
        seller.setPasswordHash(passwordEncoder.encode(resetPasswordSaveCO.getPassword()));
        sellerRepository.save(seller);

        emailSenderService.sendSimpleEmail(seller.getEmail(),
                "Your Password is Updated Successfully",
                "Password Updated");

        return new ResponseHandler(AppResponse.PASSWORD_UPDATED);
    }

    @Override
    public ResponseHandler updateAddress(String token, AddressSaveCO addressSaveCO) {

        Optional<Address> optionalAddress = addressRepository.findById(addressSaveCO.getId());

        if (!optionalAddress.isPresent()) {
            return new ResponseHandler(AppResponse.ADDRESS_NOT_FOUND);
        }
        Address address = optionalAddress.get();

        String jwtToken = token.substring(7);
        String userEmail = jwtService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(userEmail);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        if (!seller.doesContainsAddress(address)) {
            return new ResponseHandler(AppResponse.ADDRESS_NOT_FOUND);
        }

        utils.copyNonNullProperties(addressSaveCO, address);
        addressRepository.save(address);

        return new ResponseHandler(AppResponse.ADDRESS_UPDATED);
    }

    @Override
    public ResponseHandler getAllCategory() throws GlobalException {

        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.size() == 0) {
            return new ResponseHandler(AppResponse.CATEGORY_LIST_NOT_FOUND);
        }

        List<Category> leafCategoryList = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getChildCategories().size() == 0) {
                leafCategoryList.add(category);
            }
        }
        List<CategoryDTO> categoryDTOS = utils.convertToCategoryDTOList(leafCategoryList);
        return new ResponseHandler(categoryDTOS, AppResponse.OK);
    }
}
