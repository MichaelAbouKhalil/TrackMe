package com.trackme.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trackme.models.common.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class FilterUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getUsername() {
        JwtToken jwtToken = processJwt();
        return jwtToken.getUsername();
    }

    public static String getUserEmail() {
        JwtToken jwtToken = processJwt();
        return jwtToken.getEmail();
    }

    public static Long getUserId() {
        JwtToken jwtToken = processJwt();
        return jwtToken.getUserId();
    }

    public static String getUserOrgId() {
        JwtToken jwtToken = processJwt();
        return jwtToken.getOrgId();
    }

    public static String[] getUserRoles() {
        JwtToken jwtToken = processJwt();
        return jwtToken.getAuthorities();
    }

    private static JwtToken processJwt() {
        String token = getJwtToken();
        String payload = decodeJwt(token);

        JwtToken jwtToken = null;
        try {
            jwtToken = serializePayload(payload);
        } catch (IOException e) {
            log.error("failed to process jwt token with exception: {} - {}",
                    e.getClass().getSimpleName(), e.getMessage());
        }

        return jwtToken;
    }

    public static String getJwtToken() {
        String headerValue = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        return headerValue.replace("Bearer ", "");
    }

    private static String decodeJwt(String jwtToken) {
        String[] chunks = jwtToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();

        return new String(decoder.decode(chunks[1]));
    }

    private static JwtToken serializePayload(String payload) throws IOException {
        return objectMapper.readValue(payload, JwtToken.class);
    }

    public static void main(String[] args) throws IOException {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkZW1vLXBtIiwic2NvcGUiOlsid2ViY2xpZW50Il0sImV4cCI6MTYyMTExMjY2NCwidXNlcklkIjoyLCJhdXRob3JpdGllcyI6WyJST0xFX1BNIl0sImp0aSI6Ijk4ODZkYmNkLThmOWItNGQ3Ny1hMjY5LTQ0ODRmNDYxMTVhYSIsImVtYWlsIjoicG1AZGVtby5jb20iLCJvcmdJZCI6ImRlbW8tb3JnIiwiY2xpZW50X2lkIjoidHJhY2ttZS13ZWJhcHAifQ.8Fn22SpB7BEJEhKZi0h12DFMfZu1t6G-o58hClRRcvM";

        System.out.println(getUsername());
        System.out.println(getUserId());
        System.out.println(getUserEmail());
        System.out.println(getUserOrgId());
        Arrays.stream(getUserRoles()).forEach( s -> {
            System.out.println(s);
        });
    }
}
