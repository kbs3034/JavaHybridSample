package com.example.javahybridsample.web.bridge;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.example.javahybridsample.BuildConfig;
import com.example.javahybridsample.activity.base.BaseActivity;
import com.example.javahybridsample.common.RunTimePermission;
import com.example.javahybridsample.web.bridge.base.BaseNativeBridge;
import com.example.javahybridsample.web.bridge.base.RunCallbackScript;

import org.json.JSONException;
import org.json.JSONObject;

public class NativeSystem extends BaseNativeBridge {
    private final String TAG = this.getClass().getSimpleName();

    public NativeSystem(BaseActivity callerObject, WebView webView, RunCallbackScript callback){
        super(callerObject, webView, callback);
    }

    /**
     * 네이티브 시스템 기능을 호출하기 위한 브릿지
     * @param functionKey nativeSystem 그룹에서 찾을 functionKey
     * @param param : parameter
     * @return Object (String or JSONObject)
     */
    public Object exec(String functionKey, JSONObject param) throws JSONException {
        Object result = null;
        String functionStr = "nativeSystem."+functionKey;
        String callBackString = param.optString("callback", null);

        JSONObject data = param.optJSONObject("data");
        JSONObject returnValue  = new JSONObject();

        switch(functionKey) {
            case "bridgeTest" : {
                returnValue.put("msg", "bridge test!!");
                result = returnValue;
            } break;

            case "getAppVersionCode" : {
                try {
                    PackageInfo info = callerObject.getPackageManager().getPackageInfo(callerObject.getPackageName(), 0);
                    result = info.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    result = "package name not found";
                }
            } break;

            case "getAppVersionName" : {
                try {
                    PackageInfo info = callerObject.getPackageManager().getPackageInfo(callerObject.getPackageName(), 0);
                    result = info.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    result = "package name not found";
                }
            } break;

            case "buildType" : {
                Log.d(TAG, "get build type");
                result = (BuildConfig.DEBUG) ? "dev" : "prod";
            } break;

            case "copyClipboard": {
                if(!validateParam(data, new String[]{"copyStr"},callBackString)) return null;
                String str = data.optString("copyStr");
                ClipboardManager clipboardManager = (ClipboardManager) callerObject.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("nsmrt", str);
                clipboardManager.setPrimaryClip(clipData);
            } break;

            case "restart": {
                PackageManager packageManager = callerObject.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(callerObject.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                callerObject.startActivity(mainIntent);
                Runtime.getRuntime().exit(0);
            } break;

            default: {
                if(BuildConfig.DEBUG) {
                    callerObject.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String script="javascript:console.log('%s')";
                            webView.loadUrl(String.format(script,  functionStr+"은(는) 미개발된 기능 입니다. 네이티브 담당자 확인이 필요합니다."  ));
                        }
                    });
                }
            } break;
        }

        if(!TextUtils.isEmpty(callBackString)) {
            callback.runCallbackScript(callBackString, returnValue);
        } else if(result == null) {
            result = returnValue;
        }

        return result;
    }
}
