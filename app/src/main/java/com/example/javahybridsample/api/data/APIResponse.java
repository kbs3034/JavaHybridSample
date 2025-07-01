package com.example.javahybridsample.api.data;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

public class APIResponse {
    @SerializedName("type")
    public String type = "AOS";
    @SerializedName("device")
    public String device = Build.MODEL;




}
