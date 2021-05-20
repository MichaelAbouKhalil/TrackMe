package com.trackme.common.exceptionhandler;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> exceptionConverter(String errorMessage, Exception ex, WebRequest request,
                                                      HttpStatus status){

        CommonResponse<?> response = CommonResponse.error(
                (status == null) ? HttpStatus.BAD_REQUEST.value() : status.value()
                , errorMessage);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("{} occurred for request on {}", ex.getClass().getSimpleName(), requestURI);
        log.error("exception: {}", errorMessage);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = InvalidRoleException.class)
    protected ResponseEntity<Object> handleInvalidRoleException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = InvalidOperationException.class)
    protected ResponseEntity<Object> handleInvalidOperationException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = NotFoundException.class)
    protected ResponseEntity<Object> handleProjectNotFoundException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, null);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    protected ResponseEntity<Object> handleInvalidCredentialsException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UnauthorizedUserException.class)
    protected ResponseEntity<Object> handleUnauthorizedUserException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    protected ResponseEntity<Object> handleUnsupportedOperationException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        return exceptionConverter(errorMessage, ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
