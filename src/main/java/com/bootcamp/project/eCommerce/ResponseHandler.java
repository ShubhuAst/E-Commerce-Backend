package com.bootcamp.project.eCommerce;

import com.bootcamp.project.eCommerce.constants.AppResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHandler<T> {

    T data;
    String msg;
    int statusCode;

    public ResponseHandler(T data, AppResponse appResponse) {

        this.data = data;
        this.msg = appResponse.getMsg();
        this.statusCode = appResponse.getStatus().value();
    }

    public ResponseHandler(AppResponse appResponse) {

        this.msg = appResponse.getMsg();
        this.statusCode = appResponse.getStatus().value();
    }

    @Override
    public String toString() {
        return "{" +
                "msg='" + msg + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
