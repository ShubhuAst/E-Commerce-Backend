package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.HelperMethods;
import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.*;
import com.bootcamp.project.eCommerce.co_dto.saveCO.*;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.CustomerProductFilter;
import com.bootcamp.project.eCommerce.co_dto.saveCO.filters.CustomerSimilarProductFilter;
import com.bootcamp.project.eCommerce.constants.FileFor;
import com.bootcamp.project.eCommerce.constants.TokenType;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.ApplicationConstants;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.ProductVariation;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.GrantedAuthorityImpl;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.*;
import com.bootcamp.project.eCommerce.security.TokenUtil;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.FileUploadService;
import com.bootcamp.project.eCommerce.service.services.CustomerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    HelperMethods helperMethods;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AuthorizationTokenRepository tokenRepository;

    final static String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    @Override
    public ResponseHandler<UserDTO> addCustomer(CustomerSaveCO customerSaveCO, MultipartFile image) throws Exception {

        if (!customerSaveCO.getPassword().equals(customerSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }
        if (ApplicationConstants.MASTER_ADMIN.getData().equals(customerSaveCO.getEmail())) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        User user = userRepository.findByEmail(customerSaveCO.getEmail());
        if (user != null) {
            return new ResponseHandler<>(AppResponse.USER_ALREADY_EXIST);
        }

        Customer customer = modelMapper.map(customerSaveCO, Customer.class);

        GrantedAuthorityImpl grantedAuthority = new GrantedAuthorityImpl();
        grantedAuthority.setAuthority(ROLE_CUSTOMER);
        customer.setGrantedAuthorities(Collections.singletonList(grantedAuthority));

        customer.setAddress(Arrays.asList(customerSaveCO.getAddress()));

        String username = customer.getEmail();
        String password = customer.getPassword();

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        String token = tokenUtil.generateToken(customer, TokenType.ACTIVATION_TOKEN);
        customer.setAuthorizationToken(tokenUtil.convertTokenToAuthTokenObject(token));
        userRepository.save(customer);

        User savedUser = userRepository.findByEmail(customer.getEmail());

        emailSenderService.sendSimpleEmail(username,
                token,
                "Activation Token For Your Account");

        return new ResponseHandler<>(AppResponse.USER_ACCOUNT_CREATED);
    }

    @Override
    public ResponseHandler activateCustomer(String token) {

        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        if (!user.getIsActive()) {
            return new ResponseHandler(AppResponse.ACCOUNT_DISABLE);
        }

//        if (user.getAuthorizationToken().getToken() != null){
//            tokenRepository.deleteByToken(user.getAuthorizationToken().getToken());
//        }
        user.setIsActive(true);
        user.setAuthorizationToken(null);
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                "Your Account is Now Active",
                "Account Activated");

        return new ResponseHandler(AppResponse.ACCOUNT_ACTIVATED);
    }

    @Override
    public ResponseHandler reSendToken(ReSendTokenSaveCO reSendTokenSaveCO) throws Exception {

        User user = userRepository.findByEmail(reSendTokenSaveCO.getEmail());

        if (user == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        if (user.getIsActive()) {
            return new ResponseHandler(AppResponse.ACCOUNT_ALREADY_ACTIVE);
        }

//        if (user.getAuthorizationToken().getToken() != null){
//            tokenRepository.deleteByToken(user.getAuthorizationToken().getToken());
//        }
        String token = tokenUtil.generateToken(user, TokenType.ACTIVATION_TOKEN);
        user.setAuthorizationToken(tokenUtil.convertTokenToAuthTokenObject(token));
        userRepository.save(user);

        emailSenderService.sendSimpleEmail(user.getEmail(),
                token,
                "Activation Token For Your Account");

        return new ResponseHandler<>(AppResponse.RE_SEND_TOKEN);
    }

    @Override
    public ResponseHandler<UserDTO> getCustomer(String token) {

        String jwtToken = token.substring(7);
        String username = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(username);

        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        UserDTO userDTO = modelMapper.map(customer, UserDTO.class);
        return new ResponseHandler<>(userDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getAddresses(String token) {

        String jwtToken = token.substring(7);
        String username = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(username);

        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        List<Address> addresses = customer.getAddress();
        return new ResponseHandler<>(addresses, AppResponse.OK);
    }

    @Override
    public ResponseHandler updateProfile(String token, UpdateCustomerProfileSaveCO updateCustomerProfileSaveCO) {

        String jwtToken = token.substring(7);
        String username = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(username);

        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        helperMethods.copyNonNullProperties(updateCustomerProfileSaveCO, customer);
        customerRepository.save(customer);

        return new ResponseHandler(AppResponse.PROFILE_UPDATED);
    }

    @Override
    public ResponseHandler resetPassword(String token, ResetPasswordSaveCO resetPasswordSaveCO) {

        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(userEmail);
        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        if (!resetPasswordSaveCO.getPassword().equals(resetPasswordSaveCO.getConfirmPassword())) {
            return new ResponseHandler<>(AppResponse.CONFIRM_PASSWORD_MISMATCH);
        }

        if (passwordEncoder.matches(resetPasswordSaveCO.getPassword(), customer.getPassword())) {
            return new ResponseHandler<>(AppResponse.PASSWORD_CANNOT_BE_SAME_AS_PREVIOUS);
        }

//        if (customer.getAuthorizationToken().getToken() != null){
//            tokenRepository.deleteByToken(customer.getAuthorizationToken().getToken());
//        }
        customer.setAuthorizationToken(null);
        customer.setPassword(passwordEncoder.encode(resetPasswordSaveCO.getPassword()));
        customerRepository.save(customer);

        emailSenderService.sendSimpleEmail(customer.getEmail(),
                "Your Password is Updated Successfully, Login with Your New Password.",
                "Password Updated");

        return new ResponseHandler(AppResponse.PASSWORD_UPDATED);
    }

    @Override
    public ResponseHandler addAddress(String token, Address address) {

        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(userEmail);
        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        List<Address> addressList = customer.getAddress();

        for (Address addr : addressList) {
            if (address.equals(addr)) {
                return new ResponseHandler<>(AppResponse.ADDRESS_ALREADY_EXIST);
            }
        }
        addressList.add(address);
        customer.setAddress(addressList);

        customerRepository.save(customer);
        return new ResponseHandler(AppResponse.ADDRESS_ADDED);
    }

    @Override
    public ResponseHandler deleteAddress(String token, Long id) {

        Optional<Address> addressOptional = addressRepository.findById(id);

        if (!addressOptional.isPresent()) {
            return new ResponseHandler(AppResponse.ADDRESS_NOT_FOUND);
        }
        Address address = addressOptional.get();
        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(userEmail);
        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        if (!customer.getId().equals(address.getUser().getId())) {
            return new ResponseHandler(AppResponse.ADDRESS_NOT_FOUND);
        }

        addressRepository.deleteById(id);

        return new ResponseHandler(AppResponse.ADDRESS_DELETED);
    }

    @Override
    public ResponseHandler updateAddress(String token, AddressSaveCO addressSaveCO) {

        Optional<Address> optionalAddress = addressRepository.findById(addressSaveCO.getId());

        if (!optionalAddress.isPresent()) {
            return new ResponseHandler(AppResponse.ADDRESS_NOT_FOUND);
        }
        Address address = optionalAddress.get();

        String jwtToken = token.substring(7);
        String userEmail = tokenUtil.getUsernameFromToken(jwtToken);
        Customer customer = customerRepository.findByEmail(userEmail);
        if (customer == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        if (!customer.getId().equals(address.getUser().getId())) {
            return new ResponseHandler(AppResponse.ADDRESS_NOT_FOUND);
        }

        helperMethods.copyNonNullProperties(addressSaveCO, address);
        addressRepository.save(address);

        return new ResponseHandler(AppResponse.ADDRESS_UPDATED);
    }

    @Override
    public ResponseHandler getAllCategory(Long categoryId) throws GlobalException {

        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        if (categoryId == null) {
            List<Category> categoryList = categoryRepository.findAll();
            if (categoryList.size() == 0) {
                return new ResponseHandler(AppResponse.CATEGORY_LIST_NOT_FOUND);
            }
            List<Category> rootCategoryList = new ArrayList<>();
            for (Category category : categoryList) {
                if (category.getParentCategory() == null) {
                    rootCategoryList.add(category);
                }
            }
            categoryDTOS = helperMethods.convertToCategoryDTOList(rootCategoryList);
        } else {

            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (!optionalCategory.isPresent()) {
                return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
            }
            Category passedCategory = optionalCategory.get();
            if (passedCategory.getChildCategories().size() == 0) {
                return new ResponseHandler(AppResponse.CHILD_CATEGORY_NOT_FOUND);
            }
            List<Category> immediateChildOfPassedCategory = new ArrayList<>(passedCategory.getChildCategories());
            categoryDTOS = helperMethods.convertToCategoryDTOList(immediateChildOfPassedCategory);
        }
        return new ResponseHandler(categoryDTOS, AppResponse.OK);
    }

    @Override
    public ResponseHandler getFilteredCategory(Long categoryId) throws GlobalException {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
        }
        Category category = optionalCategory.get();
        List<FieldValueDTO> fieldValueDTOS =
                helperMethods.convertToCategoryDTOList(Arrays.asList(category)).get(0)
                        .getFieldAndValues();

        List<Product> productList = productRepository.findAllByCategory(category);
        Set<String> brands = new HashSet<>();
        productList.forEach(product -> brands.add(product.getBrand()));

        List<MaxMinInProduct> maxMinInProducts = new ArrayList<>();
        for (Product product : productList) {
            MaxMinInProduct mAndMinProduct = new MaxMinInProduct();
            mAndMinProduct.setId(product.getId());
            mAndMinProduct.setProductName(product.getName());

            Integer maxPrice = Integer.MIN_VALUE;
            Integer minPrice = Integer.MAX_VALUE;
            for (ProductVariation productVariation : product.getProductVariations()) {
                if (maxPrice < productVariation.getPrice()) {
                    maxPrice = productVariation.getPrice();
                }
                if (minPrice > productVariation.getPrice()) {
                    minPrice = productVariation.getPrice();
                }
            }
            if (maxPrice == Integer.MIN_VALUE){
                maxPrice = 0;
            }
            if (minPrice == Integer.MAX_VALUE){
                minPrice = 0;
            }
            mAndMinProduct.setMaximumPrice(maxPrice);
            mAndMinProduct.setMinimumPrice(minPrice);
            maxMinInProducts.add(mAndMinProduct);
        }

        CategoryFilterDTO categoryFilterDTO = new CategoryFilterDTO();
        categoryFilterDTO.setBrands(brands);
        categoryFilterDTO.setFieldValues(fieldValueDTOS);
        categoryFilterDTO.setProductMAX_MIN(maxMinInProducts);

        return new ResponseHandler(categoryFilterDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getProduct(Long id) {

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (product.getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_DELETED);
        }
        if (!product.getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_INACTIVE);
        }
        if (product.getProductVariations().size() == 0) {
            return new ResponseHandler(AppResponse.VARIATION_NOT_FOUND);
        }

        ProductDTO productDTO = helperMethods.convertToProductDTOS(Arrays.asList(product)).get(0);
        return new ResponseHandler(productDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getAllProduct(CustomerProductFilter customerProductFilter) {

        Optional<Category> optionalCategory = categoryRepository.findById(customerProductFilter.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
        }
        Category category = optionalCategory.get();
        if (category.getChildCategories().size() != 0) {
            return new ResponseHandler(AppResponse.CATEGORY_SHOULD_BE_LEAF_NODE);
        }

        List<Product> resultProductList = new ArrayList<>();
        Pageable pageable = helperMethods.filterResultPageable(customerProductFilter.getMax(),
                customerProductFilter.getOffset(),
                customerProductFilter.getSort(),
                customerProductFilter.getOrder());

        List<Product> productList = category.getProducts().stream()
                .filter(product -> product.getProductVariations().size() != 0)
                .collect(Collectors.toList());
        if (productList.size() == 0) {
            return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
        }
        if (customerProductFilter.getName() != null) {
            Optional<Product> optionalProduct = productList.stream()
                    .filter(prod -> prod.getName() == customerProductFilter.getName())
                    .findFirst();
            if (!optionalProduct.isPresent()) {
                return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
            }
            if (!optionalProduct.get().getIsActive()) {
                return new ResponseHandler(AppResponse.PRODUCT_INACTIVE);
            }
            if (optionalProduct.get().getIsDeleted()) {
                return new ResponseHandler(AppResponse.PRODUCT_DELETED);
            }
            resultProductList.add(optionalProduct.get());
        } else if (customerProductFilter.getBrand() != null) {

            List<Product> prodList = productRepository.findAllByCategoryAndBrand(category, customerProductFilter.getBrand(), pageable);
            if (prodList.size() == 0) {
                return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
            }
            resultProductList.addAll(prodList);
        } else {
            List<Product> prodList = productRepository.findAllByCategory(category, pageable);
            if (prodList.size() == 0) {
                return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
            }
            resultProductList.addAll(prodList);
        }

        List<Product> nonDeletedActiveProduct = resultProductList.stream()
                .filter(product -> !product.getIsDeleted() && product.getIsActive())
                .collect(Collectors.toList());
        if (nonDeletedActiveProduct == null) {
            return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
        }
        List<ProductDTO> productDTOS = helperMethods.convertToProductDTOS(nonDeletedActiveProduct);
        return new ResponseHandler(productDTOS, AppResponse.OK);
    }

    @Override
    public ResponseHandler getSimilarProduct(CustomerSimilarProductFilter customerSimilarProductFilter) {

        Optional<Product> productOptional = productRepository.findById(customerSimilarProductFilter.getProductId());
        if (!productOptional.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = productOptional.get();
        if (!product.getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_INACTIVE);
        }
        if (product.getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_DELETED);
        }
        Pageable pageable = helperMethods.filterResultPageable(customerSimilarProductFilter.getMax(),
                customerSimilarProductFilter.getOffset(),
                customerSimilarProductFilter.getSort(),
                customerSimilarProductFilter.getOrder());
        List<Product> productList = productRepository.findAllByCategoryAndBrand(product.getCategory(), product.getBrand(), pageable);
        if (productList == null) {
            return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
        }

        if (customerSimilarProductFilter.getName() != null) {
            Optional<Product> optionalProduct = productList.stream().filter(product1 -> product1.getName().equals(customerSimilarProductFilter.getName())).findFirst();
            if (!optionalProduct.isPresent()) {
                return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
            }
            productList = Collections.singletonList(optionalProduct.get());
        }

        List<ProductDTO> productDTOS = helperMethods.convertToProductDTOS(productList);
        return new ResponseHandler(productDTOS, AppResponse.OK);
    }
}
