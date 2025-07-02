package com.example.javahybridsample.api.data;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

/**
 * 샘플 API 응답 데이터 모델입니다.
 * 작성자: banseogg
 */
public class APIResponse {
    @SerializedName("type")
    public String type = "AOS";
    @SerializedName("device")
    public String device = Build.MODEL;




}
