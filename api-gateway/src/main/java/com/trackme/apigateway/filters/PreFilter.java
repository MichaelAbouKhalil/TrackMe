package com.trackme.apigateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class PreFilter extends ZuulFilter {

    FilterTypes preFilter = FilterTypes.PRE_FILTER;

    @Override
    public String filterType() {
        return preFilter.getType();
    }

    @Override
    public int filterOrder() {
        return preFilter.getPriority();
    }

    @Override
    public boolean shouldFilter() {
        return preFilter.getShouldFilter();
    }

    @Override
    public Object run() {
        log.debug(preFilter.getMessage());

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.debug("Request Method: {} Request URL: {}", request.getMethod(), request.getRequestURL().toString());
        return null;
    }
}
