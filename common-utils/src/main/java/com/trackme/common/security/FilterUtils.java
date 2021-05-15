package com.trackme.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackme.models.common.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class FilterUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JwtToken processJwt(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) return null;

        String payload = decodeJwt(token.replace("Bearer ", ""));

        JwtToken jwtToken = null;
        try {
            jwtToken = serializePayload(payload);
        } catch (IOException e) {
            log.error("failed to process jwt token with exception: {} - {}",
                    e.getClass().getSimpleName(), e.getMessage());
        }

        return jwtToken;
    }

    private static String decodeJwt(String jwtToken) {

        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();

        return new String(decoder.decode(chunks[1]));
    }

    private static JwtToken serializePayload(String payload) throws IOException {
        return objectMapper.readValue(payload, JwtToken.class);
    }
}
