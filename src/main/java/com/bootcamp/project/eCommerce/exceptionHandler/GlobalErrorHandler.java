package com.bootcamp.project.eCommerce.exceptionHandler;


import com.bootcamp.project.eCommerce.ResponseHandler;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalErrorHandler implements ErrorController {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e,
                                                                  WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });


        ResponseHandler responseHandler = new ResponseHandler<>(null,
                errors.toString(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @ExceptionHandler(BindException.class)
    public final ResponseEntity<Object> handleBindException(BindException e,
                                                            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });


        ResponseHandler responseHandler = new ResponseHandler<>(null,
                errors.toString(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e, WebRequest request) {

        ResponseHandler responseHandler = new ResponseHandler<>(null,
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(responseHandler, HttpStatus.valueOf(responseHandler.getStatusCode()));
    }

//    @RequestMapping("/error")
//    public ResponseEntity handleError(final HttpServletRequest request,
//                                      final HttpServletResponse response) throws IOException {
//
//        return new ResponseEntity(ResponseHandler.builder()
//                                                 .statusCode(response.getStatus())
//                                                 .msg(response.toString()).build(),
//                HttpStatus.valueOf(response.getStatus()));
//    }
}