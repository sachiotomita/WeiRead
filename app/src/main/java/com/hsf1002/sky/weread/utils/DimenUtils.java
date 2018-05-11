package com.hsf1002.sky.weread.utils;

import com.hsf1002.sky.weread.application.WeReadApplication;

/**
 * Created by hefeng on 18-5-10.
 */

public class DimenUtils {
    public static int dp2px(float dpValue){
        return (int)(dpValue * (WeReadApplication.getAppResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dp(float pxValue){
        return (int)(pxValue / (WeReadApplication.getAppResources().getDisplayMetrics().density) + 0.5f);
    }
}
