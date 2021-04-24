package com.trackme.models.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String errorMessage;

    public static ErrorResponse buildError(String message){
        return ErrorResponse.builder().errorMessage(message).build();
    }
}
