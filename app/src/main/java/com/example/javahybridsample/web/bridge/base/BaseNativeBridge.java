package com.example.javahybridsample.web.bridge.base;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.example.javahybridsample.BuildConfig;
import com.example.javahybridsample.activity.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 모든 네이티브 브릿지의 기본이 되는 추상 클래스입니다.
 * 각 브릿지는 이 클래스를 상속하여 실제 기능을 구현합니다.
 * 작성자: banseogg
 */
public abstract class BaseNativeBridge {
    public BaseActivity callerObject;
    public WebView webView;
    public RunCallbackScript callback;

    public BaseNativeBridge(BaseActivity callerObject, WebView webView, RunCallbackScript callback){
        final String TAG = this.getClass().getSimpleName();
        //브릿지 콜백함수
        this.callerObject = callerObject;
        this.webView = webView;
        this.callback = callback;
    }

    public abstract Object exec(String functionKey, JSONObject param) throws JSONException;

    // 파라미터 필수값 체크 유틸
    protected boolean validateParam(JSONObject param, String[] fields, String callBackString) {
        boolean result = true;
        String emptyField = "";
        for(String field : fields) {
            if(param == null
                    || param.opt(field) == null
                    || (param.opt(field) instanceof String
                    && TextUtils.isEmpty(param.optString(field)))) {
                result =false;
                emptyField = field;
                break;
            }
        }

        if(!result) {
            callback.runCallbackScript(callBackString,"-1","Error : "+emptyField+" 입력해주세요.", new JSONObject());
        }

        return result;
    }
}
