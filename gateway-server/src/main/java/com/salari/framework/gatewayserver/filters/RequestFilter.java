package com.salari.framework.gatewayserver.filters;

import com.netflix.zuul.context.RequestContext;
import com.salari.framework.common.utility.ApplicationProperties;

import java.util.UUID;

public class RequestFilter extends BaseFilters {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        if (RequestContext.getCurrentContext().getRequest().getMethod().equalsIgnoreCase("OPTIONS")) return false;
        if (!isPermitResources()) {
            initRequest();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object run() {
        auditRequest();
        return null;
    }

    private void initRequest() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.getZuulResponseHeaders();
        requestContext.addZuulRequestHeader("RRN", UUID.randomUUID().toString());
        requestContext.addZuulRequestHeader("URI", requestContext.getRequest().getRequestURI());
        requestContext.addZuulRequestHeader("Method", requestContext.getRequest().getMethod());
        requestContext.addZuulRequestHeader("Remote-IP", requestContext.getRequest().getRemoteAddr());
        requestContext.addZuulRequestHeader("Authorization", requestContext.getRequest().getHeader("Authorization"));
        requestContext.addZuulRequestHeader("Accept-Language", requestContext.getRequest().getHeader("Accept-Language"));
        requestContext.addZuulRequestHeader("Source", ApplicationProperties.getProperty("gateway.request.header.source"));
    }
}

