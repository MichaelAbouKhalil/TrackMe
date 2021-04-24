package com.trackme.apigateway.conf;

import com.trackme.apigateway.filters.ErrorFilter;
import com.trackme.apigateway.filters.PostFilter;
import com.trackme.apigateway.filters.PreFilter;
import com.trackme.apigateway.filters.RouteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public PreFilter preFilter() {
        return new PreFilter();
    }
    @Bean
    public PostFilter postFilter() {
        return new PostFilter();
    }
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }
    @Bean
    public RouteFilter routeFilter() {
        return new RouteFilter();
    }
}
