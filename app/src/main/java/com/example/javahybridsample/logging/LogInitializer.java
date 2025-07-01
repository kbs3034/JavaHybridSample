package com.example.javahybridsample.logging;

import android.content.Context;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;


/**
 * 로그 설정을 초기화하는 기능을 제공합니다.
 */
public class LogInitializer {
    /**
     * 로거를 초기화 합니다.
     * @param context {@link Context} 인스턴스
     */
    public static void initialize(Context context) {
        // SLF4J 초기화
        // 로그 파일을 20개까지 로테이션 (파일당 기본 최대 크기 512KB)
        FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(context);
        fileHandler.setRotateFilesCountLimit(20);
        LoggerConfiguration.configuration().addHandlerToRootLogger(fileHandler);
    }
}
