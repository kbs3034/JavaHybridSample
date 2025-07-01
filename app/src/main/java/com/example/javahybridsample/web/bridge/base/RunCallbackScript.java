package com.example.javahybridsample.web.bridge.base;

public interface RunCallbackScript {
    void runCallbackScript(String callback, String resultCode, String msg, Object returnValue);
    void runCallbackScript(String callback, Object returnValue);
}
