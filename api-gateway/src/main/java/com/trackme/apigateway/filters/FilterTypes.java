package com.trackme.apigateway.filters;

public enum FilterTypes {
    ERROR_FILTER("ERROR", 1, true, "Inside Error Filter"),
    POST_FILTER("POST", 1, true, "Inside Response Filter"),
    PRE_FILTER("PRE", 1, true, "Inside Request Filter"),
    ROUTE_FILTER("ROUTE", 1, true, "Inside Route Filter");

    private String type;
    private int priority;
    private boolean shouldFilter;
    private String message;

    FilterTypes(String type, int priority, boolean shouldFilter, String message) {
        this.type = type;
        this.priority = priority;
        this.shouldFilter = shouldFilter;
        this.message = message;
    }

    public String getType(){
        return this.type;
    }

    public int getPriority(){
        return this.priority;
    }

    public boolean getShouldFilter(){
        return this.shouldFilter;
    }

    public String getMessage(){
        return this.getMessage();
    }
}
