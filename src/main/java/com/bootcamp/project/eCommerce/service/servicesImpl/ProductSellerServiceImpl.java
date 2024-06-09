package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.ProductDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.ProductVariationDTO;
import com.bootcamp.project.eCommerce.co_dto.dto.SupportingProductDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.*;
import com.bootcamp.project.eCommerce.constants.AppData;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.FileFor;
import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.ProductVariation;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataField;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataFieldValue;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import com.bootcamp.project.eCommerce.repos.*;
import com.bootcamp.project.eCommerce.security.JWTService;
import com.bootcamp.project.eCommerce.service.EmailSenderService;
import com.bootcamp.project.eCommerce.service.FileUploadService;
import com.bootcamp.project.eCommerce.service.services.Product_SellerService;
import com.bootcamp.project.eCommerce.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSellerServiceImpl implements Product_SellerService {

    final CategoryRepository categoryRepository;
    final ProductRepository productRepository;
    final ModelMapper modelMapper;
    final JWTService JWTService;
    final SellerRepository sellerRepository;
    final EmailSenderService emailSenderService;
    final CategoryMetadataFieldValueRepository fieldValueRepository;
    final ProductVariationRepository variationRepository;
    final CategoryMetadataFieldRepository fieldRepository;
    final Utils utils;
    final FileUploadService fileUploadService;

    @Override
    public ResponseHandler addProduct(String token, ProductSaveCO productSaveCO) {

        Optional<Category> optionalCategory = categoryRepository.findById(productSaveCO.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
        }
        Category category = optionalCategory.get();
        if (category.getChildCategories().size() != 0) {
            return new ResponseHandler(AppResponse.CATEGORY_SHOULD_BE_LEAF_NODE);
        }
        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        if (!isNameUnique(productSaveCO.getName(), productSaveCO.getBrand(), category, seller)) {
            return new ResponseHandler(AppResponse.PRODUCT_NAME_IN_USE);
        }
        Product product = new Product();
        product.setName(productSaveCO.getName());
        product.setDescription(productSaveCO.getDescription());
        product.setBrand(productSaveCO.getBrand());
        product.setCategory(category);
        product.setSeller(seller);
        productRepository.save(product);

        emailSenderService.sendSimpleEmail(AppData.MASTER_ADMIN.getData(),
                "New Product with name " + product.getName() +
                        " is added By Seller With id " + seller.getId() +
                        ".\n Activate The Product using this Id",
                "New Product Added");

        return new ResponseHandler<>(AppResponse.PRODUCT_CREATED);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseHandler addProductVariation(String token, ProductVariationSaveCO productVariationSaveCO) throws IOException {

        ResponseHandler responseHandler = utils.validateImage(productVariationSaveCO.getPrimaryImage());
        if (responseHandler.getStatusCode() != 200) {
            return responseHandler;
        }
        Optional<Product> optionalProduct = productRepository.findById(productVariationSaveCO.getProductId());
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

        responseHandler = validateMetadata(product, productVariationSaveCO.getMetadata());
        if (responseHandler.getStatusCode() != 200) {
            return responseHandler;
        }

        ProductVariation productVariation = modelMapper.map(productVariationSaveCO, ProductVariation.class);
        productVariation.setProduct(product);
        productVariation.setMetadata(productVariationSaveCO.getMetadata());
        productVariation.setPrimaryImageName(productVariationSaveCO.getPrimaryImage().getOriginalFilename());
        productVariation.setId(null);
        variationRepository.save(productVariation);

        fileUploadService.uploadFile(productVariationSaveCO.getPrimaryImage(),
                productVariationSaveCO.getSecondaryImage(),
                FileFor.PRODUCT_VARIATION_FILE, productVariation.getId());

        return new ResponseHandler(AppResponse.VARIATION_ADDED);
    }

    @Override
    public ResponseHandler getProduct(String token, Long id) {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (product.getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_DELETED);
        }
        if (!product.getSeller().equals(seller)) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }

        ProductDTO productDTO = utils.convertToProductDTOS(Arrays.asList(product)).get(0);
        return new ResponseHandler(productDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getProductVariation(String token, Long id) {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        Optional<ProductVariation> variationOptional = variationRepository.findById(id);
        if (!variationOptional.isPresent()) {
            return new ResponseHandler(AppResponse.VARIATION_NOT_FOUND);
        }
        ProductVariation productVariation = variationOptional.get();
        if (productVariation.getProduct().getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_DELETED);
        }
        if (!seller.equals(productVariation.getProduct().getSeller())) {
            return new ResponseHandler(AppResponse.VARIATION_NOT_FOUND);
        }
        if (!productVariation.getProduct().getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_INACTIVE);
        }

        ProductVariationDTO variationDTO = modelMapper.map(productVariation, ProductVariationDTO.class);
        SupportingProductDTO productDTO = modelMapper.map(productVariation.getProduct(), SupportingProductDTO.class);
        variationDTO.setProduct(productDTO);

        String extension = productVariation.getPrimaryImageName().substring(productVariation.getPrimaryImageName().lastIndexOf(".") + 1);
        String imageUriString = fileUploadService.getPrimaryFile(FileFor.PRODUCT_VARIATION_FILE, productVariation.getId(), extension);
        variationDTO.setPrimaryImage(imageUriString);

        return new ResponseHandler(variationDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getAllProduct(String token, FilterSaveCO filterSaveCO) {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        if (filterSaveCO == null) {
            FilterSaveCO saveCO = new FilterSaveCO();
            saveCO.setMax(null);
            saveCO.setOffset(null);
            saveCO.setOrder(null);
            saveCO.setSort(null);
            filterSaveCO = saveCO;
        }
        Pageable pageable = utils.filterResultPageable(filterSaveCO.getMax(),
                filterSaveCO.getOffset(),
                filterSaveCO.getSort(),
                filterSaveCO.getOrder());
        List<Product> productList = productRepository.findAllBySellerAndIsDeleted(seller, false, pageable);
        if (productList.size() == 0) {
            return new ResponseHandler(AppResponse.PRODUCT_LIST_NOT_FOUND);
        }
        List<ProductDTO> productDTOS = utils.convertToProductDTOS(productList);
        return new ResponseHandler(productDTOS, AppResponse.OK);
    }

    @Override
    public ResponseHandler getAllProductVariation(String token, Long productId, FilterSaveCO filterSaveCO) {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (!seller.equals(product.getSeller())) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        if (product.getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_DELETED);
        }
        if (!product.getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_INACTIVE);
        }
        if (filterSaveCO == null) {
            FilterSaveCO saveCO = new FilterSaveCO();
            saveCO.setMax(null);
            saveCO.setOffset(null);
            saveCO.setOrder(null);
            saveCO.setSort(null);
            filterSaveCO = saveCO;
        }
        Pageable pageable = utils.filterResultPageable(filterSaveCO.getMax(),
                filterSaveCO.getOffset(),
                filterSaveCO.getSort(),
                filterSaveCO.getOrder());
        List<ProductVariation> productVariationList = variationRepository.findByProduct(product, pageable);
        if (productVariationList.size() == 0) {
            return new ResponseHandler(AppResponse.PRODUCT_VARIATION_LIST_NOT_FOUND);
        }

        List<ProductVariationDTO> variationDTOList = new ArrayList<>();
        for (ProductVariation productVariation : productVariationList) {
            ProductVariationDTO variationDTO = modelMapper.map(productVariation, ProductVariationDTO.class);

            String extension = productVariation.getPrimaryImageName().substring(productVariation.getPrimaryImageName().lastIndexOf(".") + 1);
            String imageUriString = fileUploadService.getPrimaryFile(FileFor.PRODUCT_VARIATION_FILE, productVariation.getId(), extension);
            variationDTO.setPrimaryImage(imageUriString);
            variationDTOList.add(variationDTO);
        }
        return new ResponseHandler(variationDTOList, AppResponse.OK);
    }

    @Override
    public ResponseHandler deleteProduct(String token, Long id) {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }

        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (!seller.equals(product.getSeller())) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        if (product.getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_ALREADY_DELETED);
        }
        product.setIsDeleted(true);
        productRepository.save(product);
        return new ResponseHandler(AppResponse.PRODUCT_DELETED_SUCCESS);
    }

    @Override
    public ResponseHandler updateProduct(String token, ProductUpdateSaveCO productUpdateSaveCO) {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        Optional<Product> optionalProduct = productRepository.findById(productUpdateSaveCO.getId());
        if (!optionalProduct.isPresent()) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
        Product product = optionalProduct.get();
        if (!seller.equals(product.getSeller())) {
            return new ResponseHandler(AppResponse.PRODUCT_NOT_FOUND);
        }
//      if (product.getIsDeleted()){
//           return new ResponseHandler(AppResponse.PRODUCT_DELETED);
//      }
        if (!isNameUnique(productUpdateSaveCO.getName(), product.getBrand(), product.getCategory(), seller)) {
            return new ResponseHandler(AppResponse.PRODUCT_NAME_IN_USE);
        }
        utils.copyNonNullProperties(productUpdateSaveCO, product);
        productRepository.save(product);
        return new ResponseHandler(AppResponse.PRODUCT_UPDATED);
    }

    @Override
    public ResponseHandler updateProductVariation(String token, VariationUpdateSaveCO variationUpdateSaveCO) throws IOException {

        String jwtToken = token.substring(7);
        String username = JWTService.getUsernameFromToken(jwtToken);
        Seller seller = sellerRepository.findByEmail(username);
        if (seller == null) {
            return new ResponseHandler(AppResponse.USER_NOT_FOUND);
        }
        Optional<ProductVariation> variationOptional = variationRepository.findById(variationUpdateSaveCO.getId());
        if (!variationOptional.isPresent()) {
            return new ResponseHandler(AppResponse.VARIATION_NOT_FOUND);
        }
        ProductVariation productVariation = variationOptional.get();
        if (!seller.equals(productVariation.getProduct().getSeller())) {
            return new ResponseHandler(AppResponse.VARIATION_NOT_FOUND);
        }
        if (productVariation.getProduct().getIsDeleted()) {
            return new ResponseHandler(AppResponse.PRODUCT_DELETED);
        }
        if (!productVariation.getProduct().getIsActive()) {
            return new ResponseHandler(AppResponse.PRODUCT_INACTIVE);
        }
        if (variationUpdateSaveCO.getMetadata() != null) {
            ResponseHandler responseHandler = validateMetadata(productVariation.getProduct(), variationUpdateSaveCO.getMetadata());
            if (responseHandler.getStatusCode() != 200) {
                return responseHandler;
            }
        }
        utils.copyNonNullProperties(variationUpdateSaveCO, productVariation);

        if (variationUpdateSaveCO.getPrimaryImage() != null) {
            String extension = variationUpdateSaveCO.getPrimaryImage().getContentType().substring(variationUpdateSaveCO.getPrimaryImage().getContentType().lastIndexOf("/") + 1);
            productVariation.setPrimaryImageName(productVariation.getId() + "." + extension);
            fileUploadService.uploadFile(variationUpdateSaveCO.getPrimaryImage(),
                    variationUpdateSaveCO.getSecondaryImage(),
                    FileFor.PRODUCT_VARIATION_FILE, productVariation.getId());
        }

        variationRepository.save(productVariation);
        return new ResponseHandler(AppResponse.VARIATION_UPDATED);
    }

    private boolean isNameUnique(String name, String brand, Category category, Seller seller) {

        List<Product> productList = null;
        boolean res = true;

        productList = productRepository.findAllByBrand(brand);
        res = productList.stream().anyMatch(product -> product.getName().equals(name));
        if (res) {
            return false;
        }

        productList = productRepository.findAllByCategory(category);
        res = productList.stream().anyMatch(product -> product.getName().equals(name));
        if (res) {
            return false;
        }

        productList = productRepository.findAllBySeller(seller);
        res = productList.stream().anyMatch(product -> product.getName().equals(name));
        if (res) {
            return false;
        }
        return true;
    }

    private ResponseHandler validateMetadata(Product product, String metadataString) {

        Gson gson = new Gson();
        JsonObject metadataMap = gson.fromJson(metadataString, JsonObject.class);

        if (metadataMap.size() == 0) {
            return new ResponseHandler(AppResponse.VARIATION_MUST_CONTAIN_ONE_FIELD);
        }
        ProductVariation variation = variationRepository.findByMetadataAndProduct(metadataString,
                product);
        if (variation != null) {
            return new ResponseHandler(AppResponse.VARIATION_ALREADY_EXIST);
        }

        Long categoryId = product.getCategory().getId();
        List<CategoryMetadataFieldValue> metadataFieldList = fieldValueRepository.findByCategoryId(categoryId);
        Map<String, String> fieldValues = new HashMap<>();
        List<String> fieldNames = new ArrayList<>();
        for (CategoryMetadataFieldValue metadataField : metadataFieldList) {
            Long fieldId = metadataField.getMetadataFieldValueID().getMetadataFieldId();
            CategoryMetadataField field = fieldRepository.findById(fieldId).get();
            fieldNames.add(field.getName());
            fieldValues.put(field.getName(), metadataField.getValue());
        }

        for (String key : metadataMap.keySet()) {
            String value = metadataMap.get(key).getAsString();

            if (!fieldNames.contains(key)) {
                return new ResponseHandler(key, AppResponse.METADATA_FIELD_NOT_ALLOWED);
            }
            String fieldVal = fieldValues.get(key);
            List<String> arrFieldValue = Arrays.asList(fieldVal.split(","));
            if (!arrFieldValue.contains(value)) {
                String response = key + " : " + value;
                return new ResponseHandler(response, AppResponse.METADATA_FIELD_VALUE_NOT_ALLOWED);
            }
        }
        List<ProductVariation> variationList = variationRepository.findByProduct(product);
        if (variationList.size() != 0) {
            variation = variationList.get(0);

            JsonObject metadata = gson.fromJson(variation.getMetadata(), JsonObject.class);
            if (!metadata.keySet().equals(metadataMap.keySet())) {
                return new ResponseHandler(metadata.keySet(), AppResponse.VARIATION_STRUCTURE_INVALID);
            }
        }
        return new ResponseHandler(AppResponse.OK);
    }
}
