package com.trackme.apigateway.filters;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorFilter extends ZuulFilter {

    FilterTypes errorFilter = FilterTypes.ERROR_FILTER;

    @Override
    public String filterType() {
        return errorFilter.getType();
    }

    @Override
    public int filterOrder() {
        return errorFilter.getPriority();
    }

    @Override
    public boolean shouldFilter() {
        return errorFilter.getShouldFilter();
    }

    @Override
    public Object run() {
        log.debug(errorFilter.getMessage());

        return null;
    }
}