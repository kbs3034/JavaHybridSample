package com.example.javahybridsample.api.data;

import android.os.Build;

import com.example.javahybridsample.BuildConfig;
import com.google.gson.annotations.SerializedName;

/**
 * 서버 요청 시 기본으로 포함되는 파라미터 모델입니다.
 * 작성자: banseogg
 */
public class APIRequest {
    @SerializedName("type")
    public String type = "AOS";
    @SerializedName("device")
    public String device = Build.MODEL;
    @SerializedName("version")
    public String version = BuildConfig.VERSION_NAME;


}
