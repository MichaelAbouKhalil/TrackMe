package com.trackme.models.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.trackme.models.constants.ConstantMessages;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "success",
        "message",
        "payload",
        "error"
})
public class CommonResponse<T extends Object> implements Serializable {

    private int status;
    private boolean success;
    private String message;
    private T payload;
    private ErrorResponse error;

    public static <T> CommonResponse<T> ok() {
        return ok(ConstantMessages.HTTP_OK, ConstantMessages.SUCCESS, null);
    }

    public static <T> CommonResponse<T> ok(T payload) {
        return ok(ConstantMessages.HTTP_OK, ConstantMessages.SUCCESS, payload);
    }

    public static <T> CommonResponse<T> ok(String message, T payload) {
        return ok(ConstantMessages.HTTP_OK, message, payload);
    }

    public static <T> CommonResponse<T> ok(int status, T payload) {
        return ok(status, ConstantMessages.SUCCESS, payload);
    }

    public static <T> CommonResponse<T> ok(int status, String message, T payload) {
        return CommonResponse.<T>builder()
                .success(Boolean.TRUE)
                .status(status)
                .message(message)
                .payload(payload)
                .build();
    }

    public static <T> CommonResponse<T> error() {
        return error(ConstantMessages.HTTP_BAD_REQUEST, ConstantMessages.UNKNOWN_ERROR, null);
    }

    public static <T> CommonResponse<T> error(T payload) {
        return error(ConstantMessages.HTTP_BAD_REQUEST, ConstantMessages.UNKNOWN_ERROR, payload);
    }

    public static <T> CommonResponse<T> error(String errorMessage, T payload) {
        return error(ConstantMessages.HTTP_BAD_REQUEST, errorMessage, payload);
    }

    public static <T> CommonResponse<T> error(int status, T payload) {
        return error(status, ConstantMessages.UNKNOWN_ERROR, payload);
    }

    public static <T> CommonResponse<T> error(int status, String errorMessage) {
        return error(status, errorMessage, null);
    }

    public static <T> CommonResponse<T> error(int status, String errorMessage, T payload) {
        return CommonResponse.<T>builder()
                .success(Boolean.FALSE)
                .status(status)
                .message(ConstantMessages.ERROR)
                .payload(payload)
                .error(ErrorResponse.buildError(errorMessage))
                .build();
    }
}
