package com.trackme.apigateway.filters;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteFilter extends ZuulFilter {

    FilterTypes routeFilter = FilterTypes.ROUTE_FILTER;

    @Override
    public String filterType() {
        return routeFilter.getType();
    }

    @Override
    public int filterOrder() {
        return routeFilter.getPriority();
    }

    @Override
    public boolean shouldFilter() {
        return routeFilter.getShouldFilter();
    }

    @Override
    public Object run() {
        log.debug(routeFilter.getMessage());
        return null;
    }
}