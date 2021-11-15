package com.bootcamp.project.eCommerce.constants;

import org.springframework.http.HttpStatus;

public enum AppResponse {

    CONFIRM_PASSWORD_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, "Confirm Password Mismatch"),

    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "User Already Exist"),
    USER_ACCOUNT_CREATED(HttpStatus.OK, "Account Created, Activation Token Send to Your Email"),
    SELLER_ACCOUNT_CREATED(HttpStatus.OK, "Account Created, Waiting for Admin to Approve"),
    ADMIN_ACCOUNT_CREATED(HttpStatus.OK, "Account Created"/*, Waiting for Master Admin to Approve"*/),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    SELLER_WITH_GST_ALREADY_EXIST(HttpStatus.CONFLICT, "Seller with GST Already Exist"),
    SELLER_WITH_COMPANY_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "Seller with Company Name Already Exist"),

    RE_SEND_TOKEN(HttpStatus.OK, "Successfully Resend Activation Token"),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "JWT Token has expired"),
    UNABLE_TO_GET_TOKEN(HttpStatus.FORBIDDEN, "Unable to get JWT Token"),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "Invalid Token"),
    ACTIVATION_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "JWT Token has expired, Sending New Token to Your Email"),
    ISSUE_NEW_ACCESS_TOKEN(HttpStatus.OK, "Last Token Expired, Use This New Token"),

    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "Too Many Attempts, Account is Locked"),
    ACCOUNT_DISABLE(HttpStatus.FORBIDDEN, "Account is Disabled, Activate Account First"),
    ACCOUNT_ACTIVATED(HttpStatus.OK, "Account is Now Active"),
    ACCOUNT_DEACTIVATED(HttpStatus.OK, "Account is Now De-Active"),
    ACCOUNT_PASSWORD_EXPIRED(HttpStatus.BAD_REQUEST, "Your Account Password is Expired, Reset Your Password"),

    LOGOUT(HttpStatus.OK, "Logout Successfully"),
    SEND_RESET_PASSWORD_LINK(HttpStatus.OK, "Token Has Been Send to Your Email to Reset Password"),
    PASSWORD_UPDATED(HttpStatus.OK, "Your Password is Updated Successfully, Login With Your New Password"),
    PASSWORD_CANNOT_BE_SAME_AS_PREVIOUS(HttpStatus.CONFLICT, "Password Can't be Same as Previous 3 Passwords"),
    ACCOUNT_ALREADY_ACTIVE(HttpStatus.OK, "Account is Already Active"),
    ACCOUNT_ALREADY_DE_ACTIVE(HttpStatus.OK, "Account is Already De-Active"),
    NOT_A_MASTER_ADMIN(HttpStatus.FORBIDDEN, "Only Master Admin Can Update Other Admins Profile Status"),

    PROFILE_UPDATED(HttpStatus.OK, "Your Profile Updated Successfully"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "Address Not Found"),
    ADDRESS_UPDATED(HttpStatus.OK, "Your Address Updated Successfully"),
    ADDRESS_ADDED(HttpStatus.OK, "New Address Added"),
    ADDRESS_DELETED(HttpStatus.OK, "Address deleted"),
    ADDRESS_ALREADY_EXIST(HttpStatus.CONFLICT, "Address Already Exist"),

    CUSTOMER_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "Customer List Not Found"),
    SELLER_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "Seller List Not Found"),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "This Page is Either Empty or Not Found"),

    CATEGORY_ALREADY_EXIST(HttpStatus.CONFLICT, "Category Already Exist"),
    PARENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Parent Category Not Found"),
    NEW_CATEGORY_ADDED(HttpStatus.OK, "New Category Added"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category Not Found"),
    METADATA_FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "Metadata Field Not Found"),
    CATEGORY_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "Category List Not Found"),
    CATEGORY_UPDATED(HttpStatus.OK, "Category updated Successfully"),
    CHILD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Child Category Not Found"),
    QUERY_FIELD_NOT_ACCEPTABLE(HttpStatus.BAD_REQUEST, "Some of Your Query Field Not Acceptable"),


    METADATA_FIELD_ALREADY_EXIST(HttpStatus.CONFLICT, "Metadata Field Already Exist"),
    METADATA_FIELD_ADDED(HttpStatus.OK, "Metadata Field Added Successfully"),
    DUPLICATE_VALUE_NOT_ALLOWED(HttpStatus.CONFLICT, "Duplicate Value Not Allowed"),
    METADATA_FIELD_VALUE_ADDED(HttpStatus.OK, "Metadata Field Value Added Successfully"),
    METADATA_FIELD_VALUE_UPDATED(HttpStatus.OK, "Metadata Field Value Updated Successfully"),
    METADATA_FIELD_VALUE_ALREADY_EXIST(HttpStatus.CONFLICT, "Metadata Field Value Already Exist"),

    FILE_EMPTY(HttpStatus.BAD_REQUEST, "File Can't be Empty"),
    FILE_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "File Type Not Supported"),

    PRODUCT_NAME_IN_USE(HttpStatus.CONFLICT, "Product Name is Already in Use"),
    CATEGORY_SHOULD_BE_LEAF_NODE(HttpStatus.CONFLICT, "Category Should be of Type Leaf Node"),
    PRODUCT_CREATED(HttpStatus.OK, "Product Created Successfully, Waiting for Admin to Approve"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product Not Found"),
    PRODUCT_DELETED(HttpStatus.BAD_REQUEST, "Can't access Product, Product State: Deleted"),
    PRODUCT_INACTIVE(HttpStatus.BAD_REQUEST, "Can't access Product, Product State: In-Active"),
    PRODUCT_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "Product List Not Found"),
    PRODUCT_VARIATION_LIST_NOT_FOUND(HttpStatus.NOT_FOUND, "Product Variation List Not Found"),
    PRODUCT_ALREADY_DELETED(HttpStatus.CONFLICT, "Product Already Deleted"),
    PRODUCT_DELETED_SUCCESS(HttpStatus.OK, "Product Deleted Successfully"),
    PRODUCT_UPDATED(HttpStatus.OK, "Product Updated Successfully"),
    PRODUCT_ALREADY_DE_ACTIVE(HttpStatus.CONFLICT, "Product Already De-active"),
    PRODUCT_DEACTIVATE(HttpStatus.OK, "Product Deactivate Successfully"),
    PRODUCT_ALREADY_ACTIVE(HttpStatus.CONFLICT, "Product Already Active"),
    PRODUCT_ACTIVATE(HttpStatus.OK, "Product Activate Successfully"),

    METADATA_FIELD_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Metadata Field Not Allowed"),
    METADATA_FIELD_VALUE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Metadata Field Value Not Allowed"),

    VARIATION_ADDED(HttpStatus.OK, "Product Variation Added Successfully"),
    VARIATION_ALREADY_EXIST(HttpStatus.CONFLICT, "Product Variation Already Exist"),
    VARIATION_STRUCTURE_INVALID(HttpStatus.BAD_REQUEST, "Variation Structure Invalid, Should be as Given in Data"),
    VARIATION_MUST_CONTAIN_ONE_FIELD(HttpStatus.BAD_REQUEST, "Variation Must Contain One Field"),
    VARIATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Product Variation Not Found"),
    VARIATION_UPDATED(HttpStatus.OK, "Product Variation Updated"),

    METADATA_FIELD_LIST_ADDED(HttpStatus.OK, "Product Metadata Field List Added Successfully"),

    OK(HttpStatus.OK, "Success"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error");


    private HttpStatus status;
    private String msg;

    AppResponse(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
