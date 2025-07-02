package com.example.javahybridsample.api.data.sample;

import com.google.gson.annotations.SerializedName;

/**
 * 샘플 사용자 정보를 위한 모델 클래스입니다.
 * 작성자: banseogg
 */
public class SampleUser {
    @SerializedName("name")
    public String name = "morpheus";
    @SerializedName("job")
    public String job = "leader";
}
