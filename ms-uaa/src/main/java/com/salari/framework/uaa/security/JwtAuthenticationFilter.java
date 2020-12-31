package com.salari.framework.uaa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.BaseDTOMapper;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.dto.base.MetaMapDTO;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.HttpMethods;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.repository.ApiRepository;
import com.salari.framework.uaa.repository.UserRepository;
import com.salari.framework.uaa.utility.ApplicationProperties;
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
import java.util.regex.Pattern;

import static org.hibernate.internal.util.StringHelper.isEmpty;

@Configuration
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    private final ApiRepository apiRepository;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, ApiRepository apiRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.customUserDetailsService = customUserDetailsService;
        this.apiRepository = apiRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

       /* // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response);
            return;
        }*/

        String url = request.getParameter("url");
        String method = request.getParameter("method");

        Optional<Api> api = apiRepository.findApiByUrlAndMethod(url, HttpMethods.valueOf(method));
        if (!api.isPresent()) {
            authorizationException(response, "not_found_url", HttpStatus.NOT_FOUND);
            chain.doFilter(request, response);
            return;
        }

        if (!api.get().getPublicAccess()) {
            if (!checkCurrentUserHavePermission(api.get().getId())) {
                authorizationException(response, "unauthorized_api", HttpStatus.UNAUTHORIZED);
                chain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = UserPrincipal.create(getCurrentUser(null));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
                BaseDTO.builder().metaDTO(
                        MetaDTO.builder()
                                .errors(BaseDTOMapper.getInstance().setMetaDTOCollection(null, MetaMapDTO.builder().key(errorKey).message(ApplicationProperties.getProperty(errorKey)).build()))
                                .warnings(null).build())
                        .build()
        ));
        out.flush();
    }

    private Boolean checkCurrentUserHavePermission(Integer apiId) {
        return true;
    }

    private User getCurrentUser(TokenTypes tokenTypes) {
        JwtUserDTO userToken = jwtTokenProvider.parseToken();
        if (tokenTypes != null && !userToken.getType().equals(tokenTypes))
            throw ServiceException.getInstance("unauthenticated_token", HttpStatus.UNAUTHORIZED);
        Integer userId = userToken.getId();

        return userRepository.findById(userId)
                .orElseThrow(()->ServiceException.getInstance("user-not-found",HttpStatus.NOT_FOUND));
    }
}

