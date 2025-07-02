package com.example.javahybridsample;

import android.app.Application;
import android.widget.Toast;

import com.example.javahybridsample.logging.LogInitializer;
import com.google.android.libraries.places.api.Places;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * 애플리케이션 전역 초기화를 담당하는 클래스입니다.
 * 작성자: banseogg
 */
public class App extends Application {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        logger.info("onCreate");
        INSTANCE = this;

        //app 시작시 초기화 필요한 내용 코딩
        LogInitializer.initialize(this);
    }

    public void debugToast(String message) {
        if (BuildConfig.DEBUG) Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
