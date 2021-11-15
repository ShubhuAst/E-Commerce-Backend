package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.HelperMethods;
import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.ProductCategoryDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.ProductDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.SupportingProductVariationDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.UserDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.AdminSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.FilterSaveCO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.ProductAdminFilter;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.ApplicationConstants;
import com.bootcamp.project.eCommerce.constants.FileFor;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.GrantedAuthorityImpl;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.*;
import com.bootcamp.project.eCommerce.security.TokenUtil;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.FileUploadService;
import com.bootcamp.project.eCommerce.service.services.AdminService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminServiceImpl implements AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    HelperMethods helperMethods;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    final static String ROLE_ADMIN = "ROLE_ADMIN";
    final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    final static String ROLE_SELLER = "ROLE_SELLER";

    @Override
    public ResponseHandler<UserDTO> addAdmin(AdminSaveCO adminSaveCO, MultipartFile image) throws IOException, GlobalException {

        if (!adminSaveCO.getPassword().equals(adminSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }

        if (ApplicationConstants.MASTER_ADMIN.getData().equals(adminSaveCO.getEmail())) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        User user = userRepository.findByEmail(adminSaveCO.getEmail());
        if (user != null) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        user = modelMapper.map(adminSaveCO, User.class);

        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl();
        grantedAuthority.setAuthority(ROLE_ADMIN);
        user.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        user.setAddress(Arrays.asList(adminSaveCO.getAddress()));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail());

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Account Created Successfully, Waiting for Master Admin to Approve",
                "Account Created");

        return new ResponseHandler<>(AppResponse.ADMIN_ACCOUNT_CREATED);
    }

    @Override
    public ResponseHandler getAllCustomers(String token, Map<String, String> filter) {

        if (filter.get("email") != null) {
            String email = filter.get("email");
            Customer customer = customerRepository.findByEmail(email);
            if (customer == null) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
            UserDTO userDTO = modelMapper.map(customer, UserDTO.class);
            return new ResponseHandler(userDTO, AppResponse.OK);
        } else {
            Integer max = null, offset = null;
            if (filter.get("max") != null) {
                max = Integer.valueOf(filter.get("max"));
            }
            if (filter.get("offset") != null) {
                offset = Integer.valueOf(filter.get("offset"));
            }
            String sort = filter.get("sort");
            String order = filter.get("order");
            Pageable pageable = helperMethods.filterResultPageable(max, offset, sort, order);
            Page<Customer> customerPage = customerRepository.findAll(pageable);
            if (customerPage.getContent().size() == 0) {
                return new ResponseHandler(AppResponse.PAGE_NOT_FOUND);
            }
            List<UserDTO> userDTOS = new ArrayList<>();
            for (Customer customer : customerPage) {
                UserDTO userDTO = modelMapper.map(customer, UserDTO.class);
                userDTOS.add(userDTO);
            }
            return new ResponseHandler(userDTOS, AppResponse.OK);
        }
    }

    @Override
    public ResponseHandler getAllSellers(String token, Map<String, String> filter) {

        if (filter.get("email") != null) {
            String email = filter.get("email");
            Seller seller = sellerRepository.findByEmail(email);
            if (seller == null) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
            UserDTO userDTO = modelMapper.map(seller, UserDTO.class);
            return new ResponseHandler(userDTO, AppResponse.OK);
        } else {
            Integer max = null, offset = null;
            if (filter.get("max") != null) {
                max = Integer.valueOf(filter.get("max"));
            }
            if (filter.get("offset") != null) {
                offset = Integer.valueOf(filter.get("offset"));
            }
            String sort = filter.get("sort");
            String order = filter.get("order");
            Pageable pageable = helperMethods.filterResultPageable(max, offset, sort, order);
            Page<Seller> sellerPage = sellerRepository.findAll(pageable);
            if (sellerPage.getContent().size() == 0) {
                return new ResponseHandler(AppResponse.PAGE_NOT_FOUND);
            }
            List<UserDTO> userDTOS = new ArrayList<>();
            for (Seller seller : sellerPage) {
                UserDTO userDTO = modelMapper.map(seller, UserDTO.class);
                userDTOS.add(userDTO);
            }
            return new ResponseHandler(userDTOS, AppResponse.OK);
        }
    }

    @Override
    public ResponseHandler activateUser(String token, Long id, String userType) {
        String jwtToken = token.substring(7);

        if (userType.equals(ROLE_CUSTOMER)) {
            Optional<Customer> customer = customerRepository.findById(id);
            if (!customer.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
        }
        if (userType.equals(ROLE_SELLER)) {
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

        String loggedInAdminEmail = tokenUtil.getUsernameFromToken(jwtToken);
        if (user.getGrantedAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_ADMIN))) {
            if (!ApplicationConstants.MASTER_ADMIN.getData().equals(loggedInAdminEmail)) {
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
    public ResponseHandler deActivateUser(String token, Long id, String userType) {
        String jwtToken = token.substring(7);

        if (userType.equals(ROLE_CUSTOMER)) {
            Optional<Customer> customer = customerRepository.findById(id);
            if (!customer.isPresent()) {
                return new ResponseHandler(AppResponse.USER_NOT_FOUND);
            }
        }
        if (userType.equals(ROLE_SELLER)) {
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

        String loggedInAdminEmail = tokenUtil.getUsernameFromToken(jwtToken);
        if (user.getGrantedAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_ADMIN))) {
            if (!ApplicationConstants.MASTER_ADMIN.getData().equals(loggedInAdminEmail)) {
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
        ProductDTO productDTO = helperMethods.convertToProductDTOS(Arrays.asList(product)).get(0);
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
        Pageable pageable = helperMethods.filterResultPageable(productAdminFilter.getMax(),
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
        List<ProductDTO> productDTOS = helperMethods.convertToProductDTOS(resultProductList);
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
