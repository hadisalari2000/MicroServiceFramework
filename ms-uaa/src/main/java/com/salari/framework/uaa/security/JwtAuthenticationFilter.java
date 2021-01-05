package com.salari.framework.uaa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.BaseDTOMapper;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.dto.base.MetaMapDTO;
import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.HttpMethods;
import com.salari.framework.uaa.repository.ApiRepository;
import com.salari.framework.uaa.utility.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import java.io.PrintWriter;
import java.util.Optional;

import static org.hibernate.internal.util.StringHelper.isEmpty;

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

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        /*// Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!tokenProvider.validateToken(token)) {
            throw ServiceException.getInstance("user-unauthorized", HttpStatus.UNAUTHORIZED);
        }*/

        String token=tokenProvider.getJwtTokenFromRequest(request);
        Integer userId = tokenProvider.getUserIdFromJWT(token);
        User user = customUserDetailsService.loadUserById(userId);
        UserDetails userDetails = customUserDetailsService.loadUserDetailsById(userId);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String url = request.getParameter("url");
        String method = request.getParameter("method");

        Optional<Api> api = apiRepository.findApiByUrlAndMethod(url, HttpMethods.valueOf(method));
        if (!api.isPresent()) {
            authorizationException(response, "not_found_url", HttpStatus.NOT_FOUND);
            chain.doFilter(request, response);
            return;
        }

        if (!api.get().getPublicAccess()) {
            if (!checkCurrentUserHavePermission(user, api.get().getId())) {
                authorizationException(response, "unauthorized_api", HttpStatus.UNAUTHORIZED);
                chain.doFilter(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    public void authorizationException(HttpServletResponse response, String errorKey, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatus == null ? HttpStatus.UNAUTHORIZED.value() : httpStatus.value());
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(
                BaseDTO.builder().meta(
                        MetaDTO.builder()
                                .errors(BaseDTOMapper.getInstance().setMetaDTOCollection(null, MetaMapDTO.builder().key(errorKey).message(ApplicationProperties.getProperty(errorKey)).build()))
                                .warnings(null).build())
                        .build()
        ));
        out.flush();
    }

    private Boolean checkCurrentUserHavePermission(User user, Integer apiId) {
        return true;
    }

}
