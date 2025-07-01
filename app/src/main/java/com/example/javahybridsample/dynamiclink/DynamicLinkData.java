package com.example.javahybridsample.dynamiclink;

public class DynamicLinkData {
    public static final String PREF_KEY_DEEP_LINK_URL_QUERY = "DEEP_LINK_URL_QUERY";

    public String queryString;
    public DynamicLinkData(String queryString){
        this.queryString = queryString;
    }

}
