package com.trackme.authservice.exceptionhandler;

import com.trackme.models.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {


        String errorMessage = e.getMessage();

        CommonResponse response = CommonResponse.error(HttpStatus.UNAUTHORIZED.value(), errorMessage);
        String requestURI = httpServletRequest.getRequestURI();
        log.error("authentication exception occurred for request on {}", requestURI);
        log.error("exception: {}", errorMessage);

        String jsonResponse = objectMapper.writeValueAsString(response);

        httpServletResponse.getWriter().write(jsonResponse);
    }
}
