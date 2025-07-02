package com.example.javahybridsample.web.bridge;

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
import android.widget.Toast;

import com.example.javahybridsample.BuildConfig;
import com.example.javahybridsample.activity.base.BaseActivity;

import com.example.javahybridsample.web.bridge.base.BaseNativeBridge;
import com.example.javahybridsample.web.bridge.base.RunCallbackScript;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 네이티브 영역의 공통 시스템 기능을 제공하는 브릿지 클래스입니다.
 * 웹에서 'nativeSystem' 그룹으로 호출됩니다.
 * 작성자: banseogg
 */
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
     * @author banseogg
     */
    public Object exec(String functionKey, JSONObject param) throws JSONException {
        Object result = null;
        String functionStr = "nativeSystem."+functionKey;
        String callBackString = param.optString("callback", null);

        JSONObject data = param.optJSONObject("data");
        switch(functionKey) {
            //브릿지 통신 테스트용
            case "bridgeTest" : {
                JSONObject returnValue  = new JSONObject();
                returnValue.put("msg", "bridge test!!");
                result = returnValue;
            } break;
            //현재 앱의 버전코드 반환
            case "getAppVersionCode" : {
                try {
                    PackageInfo info = callerObject.getPackageManager().getPackageInfo(callerObject.getPackageName(), 0);
                    result = info.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    result = "package name not found";
                }
            } break;
            //현재 앱의 버전 반환
            case "getAppVersionName" : {
                try {
                    PackageInfo info = callerObject.getPackageManager().getPackageInfo(callerObject.getPackageName(), 0);
                    result = info.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    result = "package name not found";
                }
            } break;
            //현재 앱의 빌드 타입 반환
            case "buildType" : {
                Log.d(TAG, "get build type");
                result = (BuildConfig.DEBUG) ? "dev" : "prod";
            } break;
            // 문자열을 클립보드에 복사
            case "copyClipboard": {
                if(!validateParam(data, new String[]{"copyStr"},callBackString)) return null;
                String str = data.optString("copyStr");
                ClipboardManager clipboardManager = (ClipboardManager) callerObject.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("nsmrt", str);
                clipboardManager.setPrimaryClip(clipData);
            } break;
            //토스트메세지 출력
            case "showToast": {
                if(!validateParam(data, new String[]{"message"},callBackString)) return null;
                String message = data.optString("message");
                Toast.makeText(callerObject, message, Toast.LENGTH_SHORT).show();
            } break;
            //디바이스 정보 반환
            case "getDeviceInfo": {
                JSONObject returnValue  = new JSONObject();
                returnValue.put("manufacturer", Build.MANUFACTURER);
                returnValue.put("model", Build.MODEL);
                returnValue.put("osVersion", Build.VERSION.RELEASE);
                result = returnValue;
            } break;
            //앱 재시작
            case "restart": {
                PackageManager packageManager = callerObject.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(callerObject.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                callerObject.startActivity(mainIntent);
                Runtime.getRuntime().exit(0);
            } break;

            //앱 종료
            case "closeApp": {
                System.exit(0);
            } break;

            default: {
                if(BuildConfig.DEBUG) {
                    callerObject.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String script="console.log('%s')";
                            webView.evaluateJavascript(String.format(script,  functionStr+"은(는) 미개발된 기능 입니다. 네이티브 담당자 확인이 필요합니다."), null);
                        }
                    });
                }
            } break;
        }

        if(!TextUtils.isEmpty(callBackString)) {
            callback.runCallbackScript(callBackString, result);
        }

        return result;
    }
}
