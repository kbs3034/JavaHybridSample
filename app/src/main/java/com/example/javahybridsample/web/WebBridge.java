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

/**
 * 웹에서 네이티브 기능을 호출하기 위한 브릿지 진입점 클래스입니다.
 * WebView에 노출되는 {@link #NativeCall(String)} 메서드를 통해 사용합니다.
 * @author banseogg
 */
public class WebBridge {
    final String TAG = this.getClass().getSimpleName();
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private Gson gson = new Gson();

    private WebView webView;
    private Activity callerObject;

    //ScriptCallback
    private RunCallbackScript callback;

    //bridge list
    private NativeSystem nativeSystem;

    public WebBridge (WebView webView, BaseActivity callerObject) {
        this.webView = webView;
        this.callerObject = callerObject;

        // 자바스크립트로 결과를 전달할 공통 콜백 구현
        // 브릿지 별로 별도의 콜백이 구현될 수 있으며,
        // 브릿지 생성자 인자로 콜백함수를 넘겨주지 않으면 브리지 자체에 정의된 콜백함수가 실행된다.
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
                            response.put("data", returnValue == null ? "" : returnValue);

                            Log.d(TAG , "콜백으로 보내는 것 >" + response);
                            if(BuildConfig.DEBUG && TextUtils.isEmpty(callbackString)) callbackString = "alert";
                            String script = callbackString +"('%s')";

                            webView.evaluateJavascript(String.format(script, response), null);
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

            // 그룹명에 따라 실제 브릿지 객체 선택
            switch(group) {
                case "nativeSystem" : bridge = nativeSystem; break;
                default: {
                    if(BuildConfig.DEBUG) {
                        callerObject.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String script="console.log('%s')";
                                webView.evaluateJavascript(String.format(script,  group +"."+functionKey+"은(는) 미개발된 기능 입니다. 네이티브 담당자 확인이 필요합니다."), null);
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
                        String script="console.log('%s')";

                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        String exceptionAsString = sw.toString();

                        webView.evaluateJavascript(String.format(script, exceptionAsString ), null);
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
