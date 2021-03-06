package com.salari.framework.msuaa.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,HttpServletResponse response,AuthenticationException e) throws IOException, ServletException {
        System.out.println(String.format("Responding with unauthorized error. Message - {}", e.getMessage()));
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Sorry, You're not authorized to access this resource.");
    }
}
