package com.salari.framework.gatewayserver.filters;

public class ResponseFilter extends BaseFilters {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        if (!isPermitResources()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object run() {
        auditResponse();
        return null;
    }
}
