package com.bootcamp.project.eCommerce.utils;

import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.*;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.constants.FileFor;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataField;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataFieldValue;
import com.bootcamp.project.eCommerce.repos.CategoryMetadataFieldRepository;
import com.bootcamp.project.eCommerce.repos.CategoryMetadataFieldValueRepository;
import com.bootcamp.project.eCommerce.service.FileUploadService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utils {

    final ModelMapper modelMapper;
    final FileUploadService fileUploadService;
    final CategoryMetadataFieldRepository metadataFieldRepository;
    final CategoryMetadataFieldValueRepository metadataFieldValueRepository;

    public void copyNonNullProperties(Object source, Object destination) {
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set emptyNames = new HashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();

        ParentCategoryDTO parentCategoryDTO = null;
        if (category.getParentCategory() != null) {
            parentCategoryDTO = modelMapper.map(category.getParentCategory(), ParentCategoryDTO.class);
        }

        List<ChildCategoryDTO> childCategoryDTOS = new ArrayList<>();
        if (category.getChildCategories() != null) {
            List<Category> childCategories = category.getChildCategories();
            for (Category childCategory : childCategories) {

                ChildCategoryDTO childCategoryDTO = modelMapper.map(childCategory, ChildCategoryDTO.class);
                childCategoryDTOS.add(childCategoryDTO);
            }
        }
        categoryDTO = modelMapper.map(category, CategoryDTO.class);
        categoryDTO.setParentCategory(parentCategoryDTO);
        categoryDTO.setChildCategories(childCategoryDTOS);

        return categoryDTO;
    }

    public Pageable filterResultPageable(Integer max, Integer offset, String sort, String order) {
        if (max == null) {
            max = 10;
        }
        if (offset == null) {
            offset = 0;
        }
        if (sort == null) {
            sort = "id";
        }
        if (order == null) {
            order = "asc";
        }
        Pageable pageable = null;

        if (order.toLowerCase().contains("desc")) {
            pageable = PageRequest.of(offset, max, Sort.by(sort).descending());
        } else {
            pageable = PageRequest.of(offset, max, Sort.by(sort).ascending());
        }
        return pageable;
    }

    public List<CategoryDTO> convertToCategoryDTOList(List<Category> categoryList) throws GlobalException {

        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = convertToCategoryDTO(category);

            List<CategoryMetadataFieldValue> metadataFieldValueList =
                    metadataFieldValueRepository.findByCategoryId(category.getId());

            List<FieldValueDTO> fieldValueDTOS = new ArrayList<>();
            for (CategoryMetadataFieldValue metadataFieldValue : metadataFieldValueList) {

                FieldValueDTO fieldValueDTO = modelMapper.map(metadataFieldValue, FieldValueDTO.class);
                Optional<CategoryMetadataField> fieldOptional = metadataFieldRepository.findById(metadataFieldValue.getMetadataFieldValueID().getMetadataFieldId());

                if (!fieldOptional.isPresent()) {
                    throw new GlobalException(AppResponse.METADATA_FIELD_NOT_FOUND);
                }
                CategoryMetadataField field = fieldOptional.get();
                fieldValueDTO.setFieldId(field.getId());
                fieldValueDTO.setFieldName(field.getName());
                fieldValueDTOS.add(fieldValueDTO);
            }
            categoryDTO.setFieldAndValues(fieldValueDTOS);
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    public ResponseHandler validateImage(MultipartFile image) {

        if (!(image.getContentType().equals("image/jpeg") ||
                image.getContentType().equals("image/jpg") ||
                image.getContentType().equals("image/png") ||
                image.getContentType().equals("image/bmp"))) {

            return new ResponseHandler(AppResponse.FILE_TYPE_NOT_SUPPORTED);
        }
        return new ResponseHandler(AppResponse.OK);
    }

    public List<ProductDTO> convertToProductDTOS(List<Product> productList) {

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product prod : productList) {
            ProductDTO productDTO = modelMapper.map(prod, ProductDTO.class);
            ProductCategoryDTO productCategoryDTO = modelMapper.map(prod.getCategory(), ProductCategoryDTO.class);
            productDTO.setProductCategory(productCategoryDTO);

            List<SupportingProductVariationDTO> variationDTOS = new ArrayList<>();
            prod.getProductVariations().forEach(productVariation -> {
                SupportingProductVariationDTO supportingProductVariationDTO = modelMapper.map(productVariation, SupportingProductVariationDTO.class);

                String extension = productVariation.getPrimaryImageName().substring(productVariation.getPrimaryImageName().lastIndexOf(".") + 1);
                String imageUriString = fileUploadService.getPrimaryFile(FileFor.PRODUCT_VARIATION_FILE, productVariation.getId(), extension);
                supportingProductVariationDTO.setImage(imageUriString);

                variationDTOS.add(supportingProductVariationDTO);
            });
            productDTO.setProductVariations(variationDTOS);
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }
}
