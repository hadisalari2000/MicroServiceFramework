package com.salari.framework.msuaa.security;

import com.salari.framework.msuaa.handler.exception.ForbiddenException;
import com.salari.framework.msuaa.handler.exception.NotFoundException;
import com.salari.framework.msuaa.model.entity.Api;
import com.salari.framework.msuaa.model.entity.User;
import com.salari.framework.msuaa.model.enums.HttpMethods;
import com.salari.framework.msuaa.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private ApiRepository apiRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        super(authenticationManager);
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String url = request.getRequestURI();
        String method = request.getMethod();

        Api api = apiRepository.findApiByUrlAndMethod(url, HttpMethods.valueOf(method)).orElseThrow(()->
                NotFoundException.getInstance(Api.class,"url", url,"method",method));

        if (!api.getPublicAccess()) {
            String token=tokenProvider.getJwtTokenFromRequest(request);
            Integer userId = tokenProvider.getUserIdFromJWT(token);
            User user = customUserDetailsService.loadUserById(userId);
            UserDetails userDetails = customUserDetailsService.loadUserDetailsById(userId);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (!checkCurrentUserHavePermission(user, api.getId())) {
                throw ForbiddenException.getInstance(Api.class);
            }
        }
        chain.doFilter(request, response);
    }

    private Boolean checkCurrentUserHavePermission(User user, Integer apiId) {
        return true;
    }

}
