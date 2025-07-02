package com.example.javahybridsample.dynamiclink;

/**
 * 다이내믹 링크 정보를 보관하는 간단한 모델입니다.
 * 작성자: banseogg
 */
public class DynamicLinkData {
    public static final String PREF_KEY_DEEP_LINK_URL_QUERY = "DEEP_LINK_URL_QUERY";

    public String queryString;
    public DynamicLinkData(String queryString){
        this.queryString = queryString;
    }

}
