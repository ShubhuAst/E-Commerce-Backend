package com.bootcamp.project.eCommerce.service.servicesImpl.admin;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.ProductDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.UserSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.UserFilterSaveCO;
import com.bootcamp.project.eCommerce.constants.AppConstants;
import com.bootcamp.project.eCommerce.constants.AppData;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.GrantedAuthorityImpl;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.*;
import com.bootcamp.project.eCommerce.security.JWTService;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.FileUploadService;
import com.bootcamp.project.eCommerce.service.services.admin.AdminService;
import com.bootcamp.project.eCommerce.utils.MapperUtils;
import com.bootcamp.project.eCommerce.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminServiceImpl implements AdminService {

    final CustomerRepository customerRepository;
    final UserRepository userRepository;
    final SellerRepository sellerRepository;
    final EmailSenderService emailSenderService;
    final MapperUtils mapperUtils;
    final FileUploadService fileUploadService;
    final JWTService JWTService;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    final CategoryRepository categoryRepository;
    final ProductRepository productRepository;

    @Override
    public ResponseHandler<UserDTO> addAdmin(UserSaveCO adminSaveCO, MultipartFile image) throws IOException, GlobalException {

        if (!adminSaveCO.getPassword().equals(adminSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }

        if (AppData.MASTER_ADMIN.getData().equals(adminSaveCO.getEmail())) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        User user = userRepository.findByEmail(adminSaveCO.getEmail());
        if (user != null) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        user = modelMapper.map(adminSaveCO, User.class);

        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl();
        grantedAuthority.setAuthority(AppConstants.ROLE_ADMIN);
        user.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        user.setAddresses(Arrays.asList(adminSaveCO.getAddress()));

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setIsActive(true);
        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail());

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Account Created Successfully, Waiting for Master Admin to Approve",
                "Account Created");

        return new ResponseHandler<>(AppResponse.ADMIN_ACCOUNT_CREATED);
    }

    @Override
    public ResponseHandler<List<UserDTO>> getAllCustomers(UserFilterSaveCO filter) {
        if (!StringUtils.isBlank(filter.getEmail())) {
            Customer customer = customerRepository.findByEmail(filter.getEmail());
            if (customer == null) {
                return new ResponseHandler<List<UserDTO>>(AppResponse.USER_NOT_FOUND);
            }
            UserDTO userDTO = modelMapper.map(customer, UserDTO.class);
            return new ResponseHandler<List<UserDTO>>(List.of(userDTO), AppResponse.OK);
        } else {
            Integer max = 10, offset = 0;
            if (filter.getMax() != null && filter.getMax() > 0) {
                max = filter.getMax();
            }
            if (filter.getOffset() != null) {
                offset = filter.getOffset();
            }
            String sort = StringUtils.isBlank(filter.getSort()) ? "email" : filter.getSort();
            String order = StringUtils.isBlank(filter.getOrder()) ? "asc" : filter.getOrder();
            Pageable pageable = mapperUtils.filterResultPageable(max, offset, sort, order);
            Page<Customer> customerPage = customerRepository.findAll(pageable);
            if (customerPage.getContent().isEmpty()) {
                return new ResponseHandler<List<UserDTO>>(AppResponse.PAGE_NOT_FOUND);
            }
            List<UserDTO> userDTOS = new ArrayList<>();
            for (Customer customer : customerPage) {
                UserDTO userDTO = modelMapper.map(customer, UserDTO.class);
                userDTOS.add(userDTO);
            }
            return new ResponseHandler<List<UserDTO>>(userDTOS, AppResponse.OK);
        }
    }

    @Override
    public ResponseHandler<List<UserDTO>> getAllSellers(UserFilterSaveCO filter) {

        if (!StringUtils.isBlank(filter.getEmail())) {
            Seller seller = sellerRepository.findByEmail(filter.getEmail());
            if (seller == null) {
                return new ResponseHandler<List<UserDTO>>(AppResponse.USER_NOT_FOUND);
            }
            UserDTO userDTO = modelMapper.map(seller, UserDTO.class);
            return new ResponseHandler<List<UserDTO>>(List.of(userDTO), AppResponse.OK);
        } else {
            Integer max = 10, offset = 0;
            if (filter.getMax() != null && filter.getMax() > 0) {
                max = filter.getMax();
            }
            if (filter.getOffset() != null) {
                offset = filter.getOffset();
            }
            String sort = StringUtils.isBlank(filter.getSort()) ? "email" : filter.getSort();
            String order = StringUtils.isBlank(filter.getOrder()) ? "asc" : filter.getOrder();
            Pageable pageable = mapperUtils.filterResultPageable(max, offset, sort, order);
            Page<Seller> sellerPage = sellerRepository.findAll(pageable);
            if (sellerPage.getContent().isEmpty()) {
                return new ResponseHandler<List<UserDTO>>(AppResponse.PAGE_NOT_FOUND);
            }
            List<UserDTO> userDTOS = new ArrayList<>();
            for (Seller seller : sellerPage) {
                UserDTO userDTO = modelMapper.map(seller, UserDTO.class);
                userDTOS.add(userDTO);
            }
            return new ResponseHandler<List<UserDTO>>(userDTOS, AppResponse.OK);
        }
    }

    @Override
    public ResponseHandler activateUser(Long id, String userType) {
        if (userType.equals(AppConstants.ROLE_CUSTOMER)) {
            Optional<Customer> customer = customerRepository.findById(id);
            if (!customer.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
        }
        if (userType.equals(AppConstants.ROLE_SELLER)) {
            Optional<Seller> sellerOptional = sellerRepository.findById(id);
            if (!sellerOptional.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        User user = userOptional.get();

        if (user.getGrantedAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AppConstants.ROLE_ADMIN))) {
            if (!AppData.MASTER_ADMIN.getData().equals(SecurityUtils.getLoggedInUsername())) {
                return new ResponseHandler(AppResponse.NOT_A_MASTER_ADMIN);
            }
        }

        if (user.getIsActive()) {
            return new ResponseHandler(AppResponse.ACCOUNT_ALREADY_ACTIVE);
        }
        user.setIsActive(true);
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Your Account is Now Active - Admin",
                "Account Activated");

        return new ResponseHandler(AppResponse.ACCOUNT_ACTIVATED);
    }

    @Override
    public ResponseHandler deActivateUser(Long id, String userType) {
        if (userType.equals(AppConstants.ROLE_CUSTOMER)) {
            Optional<Customer> customer = customerRepository.findById(id);
            if (!customer.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
        }
        if (userType.equals(AppConstants.ROLE_SELLER)) {
            Optional<Seller> sellerOptional = sellerRepository.findById(id);
            if (!sellerOptional.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        User user = userOptional.get();

        if (user.getGrantedAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AppConstants.ROLE_ADMIN))) {
            if (!AppData.MASTER_ADMIN.getData().equals(SecurityUtils.getLoggedInUsername())) {
                return new ResponseHandler(AppResponse.NOT_A_MASTER_ADMIN);
            }
        }

        if (!user.getIsActive()) {
            return new ResponseHandler(AppResponse.ACCOUNT_ALREADY_DE_ACTIVE);
        }
        user.setIsActive(false);
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Your Account is Now De-Active - Admin",
                "Account De-Activated");

        return new ResponseHandler(AppResponse.ACCOUNT_DEACTIVATED);
    }

    @Override
    public ResponseHandler getProduct(Long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        ProductDTO productDTO = mapperUtils.convertToProductDTOS(Arrays.asList(product)).get(0);
        return new ResponseHandler(productDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getAllProduct(ProductAdminFilter productAdminFilter) {

        List<Product> resultProductList = new ArrayList<>();
        if (productAdminFilter == null) {
            ProductAdminFilter saveCO = new ProductAdminFilter();
            saveCO.setMax(null);
            saveCO.setOffset(null);
            saveCO.setOrder(null);
            saveCO.setSort(null);
            productAdminFilter = saveCO;
        }
        Pageable pageable = mapperUtils.filterResultPageable(productAdminFilter.getMax(),
                productAdminFilter.getOffset(),
                productAdminFilter.getSort(),
                productAdminFilter.getOrder());

        if (productAdminFilter.getName() != null) {
            List<Product> productList = productRepository.findAllByName(productAdminFilter.getName(), pageable);
            if (productList == null) {
                return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
            }
            resultProductList.addAll(productList);
        } else if (productAdminFilter.getCategoryId() != null) {
            Optional<Category> optionalCategory = categoryRepository.findById(productAdminFilter.getCategoryId());
            if (!optionalCategory.isPresent()) {
                return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
            }
            List<Product> productList = productRepository.findAllByCategory(optionalCategory.get(), pageable);
            if (productList == null) {
                return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
            }
            resultProductList.addAll(productList);
        } else if (productAdminFilter.getSellerId() != null) {
            Optional<Seller> optionalSeller = sellerRepository.findById(productAdminFilter.getSellerId());
            if (!optionalSeller.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
            List<Product> productList = productRepository.findAllBySeller(optionalSeller.get(), pageable);
            if (productList == null) {
                return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
            }
            resultProductList.addAll(productList);
        } else {
            Page<Product> productList = productRepository.findAll(pageable);
            if (productList.getContent().size() == 0) {
                return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
            }
            resultProductList.addAll(productList.getContent());
        }
        List<ProductDTO> productDTOS = mapperUtils.convertToProductDTOS(resultProductList);
        return new ResponseHandler(productDTOS, AppResponse.OK);
    }

    @Override
    public ResponseHandler deactivateProduct(Long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (!product.getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_ALREADY_DE_ACTIVE);
        }
        product.setIsActive(false);
        productRepository.save(product);
        return new ResponseHandler(AppResponse.PRODUCT_DEACTIVATE);
    }

    @Override
    public ResponseHandler activateProduct(Long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (product.getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_ALREADY_ACTIVE);
        }
        product.setIsActive(true);
        productRepository.save(product);
        return new ResponseHandler(AppResponse.PRODUCT_ACTIVATE);
    }
}
