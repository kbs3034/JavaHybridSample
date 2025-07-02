package com.example.javahybridsample.util;

import android.content.res.Resources;

/**
 * 화면 단위 변환 등을 제공하는 공통 유틸 클래스입니다.
 * 작성자: banseogg
 */
public class CommonUtil {
    /**
     * toPx
     */
    public static int getPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
