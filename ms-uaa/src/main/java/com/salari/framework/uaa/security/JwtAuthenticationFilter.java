package com.salari.framework.uaa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salari.framework.uaa.handler.exception.ServiceException;
import com.salari.framework.uaa.model.dto.base.BaseDTO;
import com.salari.framework.uaa.model.dto.base.MetaDTO;
import com.salari.framework.uaa.model.dto.user.JwtUserDTO;
import com.salari.framework.uaa.model.entity.Api;
import com.salari.framework.uaa.model.entity.User;
import com.salari.framework.uaa.model.enums.HttpMethods;
import com.salari.framework.uaa.model.enums.TokenTypes;
import com.salari.framework.uaa.repository.ApiRepository;
import com.salari.framework.uaa.utility.ApplicationUtilities;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.regex.Pattern;

@Configuration
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {


    private final ApiRepository apiRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, ApiRepository apiRepository) {
        super(authenticationManager);
        this.customUserDetailsService = customUserDetailsService;
        this.apiRepository = apiRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request.getMethod().equalsIgnoreCase("options")) return;
        if (request.getRequestURI().equals("/v1/resource")) {

            String url = request.getParameter("url");
            String method = request.getParameter("method");

            Optional<Api> apiOptional = apiRepository.findApiByUrlAndMethod(url, HttpMethods.valueOf(method));
            if (!apiOptional.isPresent()) {
                authorizationException(response, "not_found_url", HttpStatus.NOT_FOUND);
                return;
            }
            Api api = apiOptional.get();

            if (!api.getPublicAccess()) {
                if (!checkCurrentUserHavePermission(api.getId())) {
                    authorizationException(response, "unauthorized_api_channel", HttpStatus.UNAUTHORIZED);
                    return;
                }

                UserDetails userDetails = GlobalService.getCurrentUserDetails();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);


            }
            chain.doFilter(request, response);
        }
    }

    public static void authorizationException(HttpServletResponse response, String errorKey, HttpStatus httpStatus) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatus == null ? HttpStatus.UNAUTHORIZED.value() : httpStatus.value());
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(
                BaseDTO.builder().metaDTO(
                        MetaDTO.builder()
                                .errors(BaseDTOMapper.getInstance().setMetaDTOCollection(null,
                                        MetaMapDTO.builder()
                                                .key(errorKey)
                                                .message(Translator.toLocale(errorKey)
                                                ).build()))
                                .warnings(null).build())
                        .build()
        ));
        out.flush();
    }

    private Boolean isPermitResources(HttpServletRequest request) {
        Boolean result = null;
        // -> Check path of request in WhiteListURLs
        // -> If return false that means current path no need to log(audit) filter
        String[] ignoreRoutes = {
                ".*/api/godar-aggregator/v2/api-docs",
                ".*/swagger-ui",
                ".*/swagger-resources",
                ".*/configuration/security",
                ".*/v2/api-docs",
                ".*/configuration/ui",
                ".*/swagger-ui.html*",
                ".*/webjars/springfox-swagger-ui/*.*"
        };
        for (String ignoreRoute : ignoreRoutes) {
            Pattern pattern = Pattern.compile(ignoreRoute.trim());
            if (pattern.matcher(request.getRequestURI()).matches()) {
                result = true;
                break;
            }
        }
        return result != null;
    }

    private Boolean checkCurrentUserHavePermission(Integer apiId) {
        return true;
    }
    private User getCurrentUser(TokenTypes tokenTypes) {
        JwtUserDTO userToken = jwtTokenProvider.parseToken();
        if (tokenTypes != null && !userToken.getType().equals(tokenTypes))
            throw ServiceException.getInstance("unauthenticated_token", HttpStatus.UNAUTHORIZED);
        Integer userId = userToken.getId();
        return userRepository.findById(userId);
    }
}

