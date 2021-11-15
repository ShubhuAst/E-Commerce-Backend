package com.bootcamp.project.eCommerce.service.servicesImpl;

import com.bootcamp.project.eCommerce.HelperMethods;
import com.bootcamp.project.eCommerce.ResponseHandler;
import com.bootcamp.project.eCommerce.co_dto.dto.CategoryDTO;
import com.bootcamp.project.eCommerce.co_dto.saveCO.MetadataFieldValueSaveCO;
import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.bootcamp.project.eCommerce.exceptionHandler.GlobalException;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataField;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataFieldValue;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.CategoryMetadataFieldValueID;
import com.bootcamp.project.eCommerce.repos.CategoryMetadataFieldRepository;
import com.bootcamp.project.eCommerce.repos.CategoryMetadataFieldValueRepository;
import com.bootcamp.project.eCommerce.repos.CategoryRepository;
import com.bootcamp.project.eCommerce.service.services.Category_AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryAdminServiceImpl implements Category_AdminService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetadataFieldRepository metadataFieldRepository;

    @Autowired
    CategoryMetadataFieldValueRepository metadataFieldValueRepository;

    @Autowired
    HelperMethods helperMethods;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ResponseHandler addCategory(String name, Long parentID) {

        Category category = new Category();

        if (parentID != null) {
            Optional<Category> optionalParentCategory = categoryRepository.findById(parentID);
            if (!optionalParentCategory.isPresent()) {
                return new ResponseHandler(AppResponse.PARENT_CATEGORY_NOT_FOUND);
            }
            Category parentCategory = optionalParentCategory.get();
            category.setParentCategory(parentCategory);
        }

        if (!isCategoryNameUnique(name, parentID)) {
            return new ResponseHandler(AppResponse.CATEGORY_ALREADY_EXIST);
        }
        category.setName(name);
        categoryRepository.save(category);
        return new ResponseHandler(AppResponse.NEW_CATEGORY_ADDED);
    }

    @Override
    public ResponseHandler getCategory(Long id) throws GlobalException {

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
        }
        Category category = optionalCategory.get();
        CategoryDTO categoryDTO = helperMethods.convertToCategoryDTOList(Arrays.asList(category)).get(0);
        return new ResponseHandler(categoryDTO, AppResponse.OK);
    }

    @Override
    public ResponseHandler getAllCategory(Map<String, String> filter) throws GlobalException {

        List<Category> categoryList = new ArrayList<>();
        if (filter.get("id") != null) {
            Long id = Long.valueOf(filter.get("id"));
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (!optionalCategory.isPresent()) {
                return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
            }
            Category category = optionalCategory.get();
            categoryList.add(category);
        } else if (filter.get("name") != null) {
            String name = filter.get("name");
            Category category = categoryRepository.findByName(name);
            if (category == null) {
                return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
            }
            categoryList.add(category);
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
            Page<Category> categoryPage = categoryRepository.findAll(pageable);
            if (categoryPage.getContent().size() == 0) {
                return new ResponseHandler(AppResponse.PAGE_NOT_FOUND);
            }
            for (Category category : categoryPage) {
                categoryList.add(category);
            }
        }
        List<CategoryDTO> categoryDTOS = helperMethods.convertToCategoryDTOList(categoryList);
        return new ResponseHandler(categoryDTOS, AppResponse.OK);
    }

    @Override
    public ResponseHandler updateCategory(Long id, String name) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
        }
        Category category = optionalCategory.get();
        if (!isCategoryNameUnique(name, category.getParentCategory().getId())) {
            return new ResponseHandler(AppResponse.CATEGORY_ALREADY_EXIST);
        }
        category.setName(name);
        categoryRepository.save(category);
        return new ResponseHandler(AppResponse.CATEGORY_UPDATED);
    }

    @Override
    public ResponseHandler addMetadataField(String name) {

        CategoryMetadataField metadataField = metadataFieldRepository.findByName(name);
        if (metadataField != null) {
            return new ResponseHandler(AppResponse.METADATA_FIELD_ALREADY_EXIST);
        }
        metadataField = new CategoryMetadataField();
        metadataField.setName(name);
        metadataFieldRepository.save(metadataField);
        return new ResponseHandler(AppResponse.METADATA_FIELD_ADDED);
    }

    @Override
    public ResponseHandler getAllMetadataField(Map<String, String> filter) {

        if (filter.get("id") != null) {
            Long id = Long.valueOf(filter.get("id"));
            Optional<CategoryMetadataField> metadataFieldOptional = metadataFieldRepository.findById(id);
            if (!metadataFieldOptional.isPresent()) {
                return new ResponseHandler(AppResponse.METADATA_FIELD_NOT_FOUND);
            }
            CategoryMetadataField metadataField = metadataFieldOptional.get();
            return new ResponseHandler(metadataField, AppResponse.OK);
        } else if (filter.get("name") != null) {
            String name = filter.get("name");
            CategoryMetadataField metadataField = metadataFieldRepository.findByName(name);
            if (metadataField == null) {
                return new ResponseHandler(AppResponse.METADATA_FIELD_NOT_FOUND);
            }
            return new ResponseHandler(metadataField, AppResponse.OK);
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
            Page<CategoryMetadataField> metadataFieldPage = metadataFieldRepository.findAll(pageable);
            if (metadataFieldPage.getContent().size() == 0) {
                return new ResponseHandler(AppResponse.PAGE_NOT_FOUND);
            }
            return new ResponseHandler(metadataFieldPage.getContent(), AppResponse.OK);
        }
    }

    @Override
    public ResponseHandler addUpdateMetadataFieldValue(MetadataFieldValueSaveCO metadataFieldValueSaveCO, String requestType) {

        Optional<Category> optionalCategory = categoryRepository.findById(metadataFieldValueSaveCO.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ResponseHandler(AppResponse.CATEGORY_NOT_FOUND);
        }
        Category category = optionalCategory.get();
        Optional<CategoryMetadataField> optionalMetadataField =
                metadataFieldRepository.findById(metadataFieldValueSaveCO.getMetadataFieldId());
        if (!optionalMetadataField.isPresent()) {
            return new ResponseHandler(AppResponse.METADATA_FIELD_NOT_FOUND);
        }
        CategoryMetadataField categoryMetadataField = optionalMetadataField.get();
        String values = metadataFieldValueSaveCO.getValues();

        CategoryMetadataFieldValueID metadataFieldValueID = new CategoryMetadataFieldValueID(metadataFieldValueSaveCO.getCategoryId(),
                metadataFieldValueSaveCO.getMetadataFieldId());

        Optional<CategoryMetadataFieldValue> fieldValueOptional = metadataFieldValueRepository.findById(metadataFieldValueID);
        if (fieldValueOptional.isPresent()) {
            if (requestType.equals("POST")) {
                return new ResponseHandler(AppResponse.METADATA_FIELD_VALUE_ALREADY_EXIST);
            } else {
                CategoryMetadataFieldValue fieldValue = fieldValueOptional.get();
                values = values.concat(",").concat(fieldValue.getValues_());
            }
        }
        String[] arrValues = values.split(",");
        Set<String> checkDuplicate = new HashSet<>();
        for (String val : arrValues) {
            if (!checkDuplicate.add(val)) {
                return new ResponseHandler(AppResponse.DUPLICATE_VALUE_NOT_ALLOWED);
            }
        }
        CategoryMetadataFieldValue metadataFieldValue = new CategoryMetadataFieldValue();
        metadataFieldValue.setMetadataFieldValueID(metadataFieldValueID);
        metadataFieldValue.setValues_(values);

        metadataFieldValueRepository.save(metadataFieldValue);

        if (requestType.equals("POST")) {
            return new ResponseHandler(AppResponse.METADATA_FIELD_VALUE_ADDED);
        }
        return new ResponseHandler(AppResponse.METADATA_FIELD_VALUE_UPDATED);
    }

    private boolean isCategoryNameUnique(String name, Long id) {

        List<Category> rootLevelCategory = categoryRepository.findAllByParentCategory(null);
        for (Category category : rootLevelCategory) {
            if (name.equals(category.getName())) {
                return false;
            }
        }
        if (id != null) {
            Category category = categoryRepository.findById(id).get();
            for (Category childCategory : category.getChildCategories()) {
                if (name.equals(childCategory.getName())) {
                    return false;
                }
            }
        }
        return true;
    }
}
