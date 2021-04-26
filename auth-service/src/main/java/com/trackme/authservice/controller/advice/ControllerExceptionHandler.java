package com.trackme.authservice.controller.advice;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.exception.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        CommonResponse response = CommonResponse.error(status.value(), errorMessage);

        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("request validation error occurred for request on {}", requestURI);
        log.error("exception: {}", errorMessage);

        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(Exception ex, WebRequest request) {

        String errorMessage = ex.getMessage();

        CommonResponse response = CommonResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("user already exist exception occurred for request on {}", requestURI);
        log.error("exception: {}", errorMessage);

        return ResponseEntity.ok().body(response);
    }
}
