package com.trackme.common.exceptionhandler;

import com.trackme.models.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       AccessDeniedException e) throws IOException, ServletException {

        String errorMessage = e.getMessage();

        CommonResponse<?> response = CommonResponse.error(HttpStatus.FORBIDDEN.value(), errorMessage);
        String requestURI = httpServletRequest.getRequestURI();
        log.error("authentication exception occurred for request on {}", requestURI);
        log.error("exception: {}", errorMessage);

        String jsonResponse = objectMapper.writeValueAsString(response);

        httpServletResponse.getWriter().write(jsonResponse);
    }
}
