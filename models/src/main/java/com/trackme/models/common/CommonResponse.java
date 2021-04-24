package com.trackme.models.common;

import com.trackme.models.constants.ConstantMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse {

    private int status;
    private boolean isSuccess;
    private String message;
    private Object payload;
    private ErrorResponse error;

    public static CommonResponse ok() {
        return ok(ConstantMessages.HTTP_OK, ConstantMessages.SUCCESS, null);
    }

    public static CommonResponse ok(Object payload) {
        return ok(ConstantMessages.HTTP_OK, ConstantMessages.SUCCESS, payload);
    }

    public static CommonResponse ok(String message, Object payload) {
        return ok(ConstantMessages.HTTP_OK, message, payload);
    }

    public static CommonResponse ok(int status, Object payload) {
        return ok(status, ConstantMessages.SUCCESS, payload);
    }

    public static CommonResponse ok(int status, String message, Object payload) {
        return CommonResponse.builder()
                .isSuccess(Boolean.TRUE)
                .status(status)
                .message(message)
                .payload(payload)
                .build();
    }

    public static CommonResponse error() {
        return error(ConstantMessages.HTTP_BAD_REQUEST, ConstantMessages.UNKNOWN_ERROR, null);
    }

    public static CommonResponse error(Object payload) {
        return error(ConstantMessages.HTTP_BAD_REQUEST, ConstantMessages.UNKNOWN_ERROR, payload);
    }

    public static CommonResponse error(String errorMessage, Object payload) {
        return error(ConstantMessages.HTTP_BAD_REQUEST, errorMessage, payload);
    }

    public static CommonResponse error(int status, Object payload) {
        return error(status, ConstantMessages.UNKNOWN_ERROR, payload);
    }

    public static CommonResponse error(int status, String errorMessage, Object payload) {
        return CommonResponse.builder()
                .isSuccess(Boolean.FALSE)
                .status(status)
                .message(ConstantMessages.ERROR)
                .payload(payload)
                .error(ErrorResponse.buildError(errorMessage))
                .build();
    }
}
