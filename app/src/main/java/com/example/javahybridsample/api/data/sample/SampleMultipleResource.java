package com.example.javahybridsample.api.data.sample;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 여러 리소스 목록을 표현하는 샘플 모델입니다.
 * 작성자: banseogg
 */
public class SampleMultipleResource {
    @SerializedName("page")
    public Integer page;
    @SerializedName("per_page")
    public Integer perPage;
    @SerializedName("total")
    public Integer total;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("data")
    public List<Datum> data = null;

    public class Datum {

        @SerializedName("id")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("year")
        public Integer year;
        @SerializedName("pantone_value")
        public String pantoneValue;

    }
}
