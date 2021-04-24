package com.trackme.apigateway.filters;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostFilter extends ZuulFilter {

    FilterTypes postFilter = FilterTypes.POST_FILTER;

    @Override
    public String filterType() {
        return postFilter.getType();
    }

    @Override
    public int filterOrder() {
        return postFilter.getPriority();
    }

    @Override
    public boolean shouldFilter() {
        return postFilter.getShouldFilter();
    }

    @Override
    public Object run() {
        log.debug(postFilter.getMessage());

        return null;
    }
}
