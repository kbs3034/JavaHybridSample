package com.example.javahybridsample.util;

import android.content.res.Resources;

public class CommonUtil {
    /**
     * toPx
     */
    public static int getPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

}
