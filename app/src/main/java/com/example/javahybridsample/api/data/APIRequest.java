package com.example.javahybridsample.api.data;

import android.os.Build;

import com.example.javahybridsample.BuildConfig;
import com.google.gson.annotations.SerializedName;

public class APIRequest {
    @SerializedName("type")
    public String type = "AOS";
    @SerializedName("device")
    public String device = Build.MODEL;
    @SerializedName("version")
    public String version = BuildConfig.VERSION_NAME;


}
