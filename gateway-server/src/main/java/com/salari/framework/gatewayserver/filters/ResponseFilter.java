package com.salari.framework.gatewayserver.filters;

public class ResponseFilter extends BaseFilters {

    @Override
    public String filterType() {
        return POST_FILTER;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
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
