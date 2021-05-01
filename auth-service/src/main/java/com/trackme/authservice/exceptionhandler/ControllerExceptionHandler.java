package com.trackme.authservice.exceptionhandler;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.exception.InvalidRoleException;
import com.trackme.models.exception.UserAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> exceptionConverter(String errorMessage, Exception ex, WebRequest request){

        CommonResponse response = CommonResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("{} occurred for request on {}", ex.getClass().getSimpleName(), requestURI);
        log.error("exception: {}", errorMessage);

        return ResponseEntity.ok().body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

    @ExceptionHandler(value = InvalidRoleException.class)
    protected ResponseEntity<Object> handleInvalidRoleException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

    @ExceptionHandler(value = InvalidOperationException.class)
    protected ResponseEntity<Object> handleInvalidOperationException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

    @ExceptionHandler(value = UnauthorizedUserException.class)
    protected ResponseEntity<Object> handleUnauthorizedUserException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request);
    }

}
