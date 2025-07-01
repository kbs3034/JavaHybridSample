package com.example.javahybridsample.web;

import android.app.Activity;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.example.javahybridsample.BuildConfig;
import com.example.javahybridsample.activity.base.BaseActivity;
import com.example.javahybridsample.web.bridge.NativeSystem;
import com.example.javahybridsample.web.bridge.base.BaseNativeBridge;
import com.example.javahybridsample.web.bridge.base.RunCallbackScript;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class WebBridge {
    final String TAG = this.getClass().getSimpleName();
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private Gson gson = new Gson();

    private WebView webView;
    private Activity callerObject;

    //ScriptCallback
    private RunCallbackScript callback;

    //bridge
    private NativeSystem nativeSystem;

    public WebBridge (WebView webView, BaseActivity callerObject) {
        this.webView = webView;
        this.callerObject = callerObject;

        //callback
        this.callback = new RunCallbackScript() {
            @Override
            public void runCallbackScript(String callback, String resultCode, String msg, Object returnValue) {
                callerObject.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String callbackString = callback;
                            JSONObject response = new JSONObject();
                            response.put("status",new JSONObject("{\"code\":\""+resultCode+"\",\"msg\":\""+msg+"\"}"));
                            response.put("data",returnValue == null ? new JSONObject() : returnValue);

                            Log.d(TAG , "콜백으로 보내는 것 >" + response);
                            if(BuildConfig.DEBUG && TextUtils.isEmpty(callbackString)) callbackString = "alert";
                            String script="javascript:("+ callbackString +"('%s'))";

                            //백슬래쉬 하나씩 줄어들어서 화면에서 에러 발생. 나중에 다른 방법 찾아보면 좋을 듯
                            webView.loadUrl(String.format(script, response.toString().replace("\\","\\\\")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void runCallbackScript(String callback, Object returnValue) {
                this.runCallbackScript(callback, "0", "처리 완료되었습니다.", returnValue);
            }
        };

        //각 기능별 브릿지 생성
        this.nativeSystem = new NativeSystem(callerObject, webView, this.callback);
    }

    public static final String DEFAULT_WEB_BRIDGE_NAME = "androidWebBridge";


    /**
     * 웹에서 네이티브 기능을 호출하기 위한 브릿지 공통부
     * @param jsonData ex) {'group' : 'nativeSystem', 'functionKey' : 'test', 'callback' : "testBridgeCallback" ... }
     * @return (String or JSONString)
     */
    @JavascriptInterface
    public String NativeCall(String jsonData, String callback) {
        try {
            jsonData = new JSONObject(jsonData).put("callback", callback).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NativeCall(jsonData);
    }

    /**
     * 웹에서 네이티브 기능을 호출하기 위한 브릿지 공통부
     * @param jsonData ex) {'group' : 'nativeSystem', 'functionKey' : 'test', 'callback' : "testBridgeCallback" ... }
     * @return (String or JSONString)
     */
    @JavascriptInterface
    public String NativeCall(String jsonData) {
        Object result = null;
        JSONObject param = null;

        try {
            param  = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            String group = param.getString("group");
            String functionKey = param.getString("functionKey");

            BaseNativeBridge bridge = null;

            switch(group) {
                case "nativeSystem" : bridge = nativeSystem; break;
                default: {
                    if(BuildConfig.DEBUG) {
                        callerObject.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String script="javascript:console.log('%s')";
                                webView.loadUrl(String.format(script,  group +"."+functionKey+"은(는) 미개발된 기능 입니다. 네이티브 담당자 확인이 필요합니다."));
                            }
                        });
                    }
                }break;
            }

            if(bridge != null) result = bridge.exec(functionKey, param);

        } catch (Exception e) {
            e.printStackTrace();
            if(BuildConfig.DEBUG) {
                callerObject.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String script="javascript:console.log('%s')";

                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();

                        webView.loadUrl(String.format(script, exceptionAsString ));
                    }
                });
            }

            if(param != null && param.has("callback")) {
                callback.runCallbackScript(param.optString("callback"),"-1", "처리 실패하였습니다.", null);
            }
        }

        //return type : JSONObject, String else null
        return (result instanceof JSONObject) ? ((JSONObject)result).toString() : (result instanceof String) ? (String)result : null;
    }

}
