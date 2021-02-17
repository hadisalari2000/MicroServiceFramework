package com.salari.framework.gatewayserver.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.salari.framework.common.model.audit.domain.BaseAuditItemRequest;
import com.salari.framework.common.model.audit.enums.RequestStatus;
import com.salari.framework.common.utility.LogBackUtility;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.regex.Pattern;

public class BaseFilters extends ZuulFilter {

    @Autowired
    protected Logger logger;

    @Override
    public String filterType() {
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        return null;
    }

    protected void auditResponse() {
        LogBackUtility.getInstance(logger).log(
                BaseAuditItemRequest.builder()
                        .rrn(RequestContext.getCurrentContext().getZuulRequestHeaders().get("rrn"))
                        .responseTime(new Date().getTime())
                        .requestStatus(RequestContext.getCurrentContext().getResponse().getStatus() >= HttpStatus.SC_ACCEPTED ? RequestStatus.FAILED : RequestStatus.SUCCESS)
                        .build(),
                Level.DEBUG
        );
    }

    protected void auditRequest() {
        LogBackUtility.getInstance(logger).log(
                BaseAuditItemRequest.builder()
                        .rrn(RequestContext.getCurrentContext().getZuulRequestHeaders().get("rrn"))
                        .remoteIp(RequestContext.getCurrentContext().getZuulRequestHeaders().get("remoteIp"))
                        .route(RequestContext.getCurrentContext().getZuulRequestHeaders().get("route"))
                        .userId(RequestContext.getCurrentContext().getZuulRequestHeaders().get("userId"))
                        .method(RequestContext.getCurrentContext().getZuulRequestHeaders().get("method") != null ?
                                RequestContext.getCurrentContext().getZuulRequestHeaders().get("method").toLowerCase() :
                                RequestContext.getCurrentContext().getRequest().getMethod().toLowerCase())
                        .requestTime(new Date().getTime())
                        .requestStatus(RequestStatus.UNKNOWN)
                        .build(),
                Level.DEBUG
        );
    }

    protected Boolean isPermitResources() {
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
        String requestUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        for (String ignoreRoute : ignoreRoutes) {
            Pattern pattern = Pattern.compile(ignoreRoute.trim());
            if (pattern.matcher(requestUri).matches())
                return true;
        }
        return false;
    }
}