package com.bootcamp.project.eCommerce.exceptionHandler;

import com.bootcamp.project.eCommerce.constants.AppResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GlobalException extends Exception {

    String msg;
    String details;
    int statusCode;

    public GlobalException(AppResponse appResponse) {
        this.msg = appResponse.getMsg();
        this.statusCode = appResponse.getStatus().value();
    }

    public GlobalException(String msg, String details, int statusCode) {
        this.msg = msg;
        this.details = details;
        this.statusCode = statusCode;
    }
}
