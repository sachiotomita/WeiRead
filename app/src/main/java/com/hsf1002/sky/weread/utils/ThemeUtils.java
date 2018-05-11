package com.hsf1002.sky.weread.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.application.WeReadApplication;
import com.hsf1002.sky.weread.widget.theme.Theme;

/**
 * Created by hefeng on 18-5-9.
 */

public class ThemeUtils {

    public static int getThemeColor2Array(Context context, int attrRes) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attrRes});
        int color = typedArray.getColor(0, 0xffffff);
        typedArray.recycle();
        return color;
    }

    public static int getThemeColor() {
        Theme theme = SharedPreUtils.getInstance().getCurrentTheme();

        switch (theme) {
            case Blue:
                return WeReadApplication.getAppResources().getColor(R.color.colorBluePrimary);
            case Red:
                return WeReadApplication.getAppResources().getColor(R.color.colorRedPrimary);
            case Brown:
                return WeReadApplication.getAppResources().getColor(R.color.colorBrownPrimary);
            case Green:
                return WeReadApplication.getAppResources().getColor(R.color.colorGreenPrimary);
            case Purple:
                return WeReadApplication.getAppResources().getColor(R.color.colorPurplePrimary);
            case Teal:
                return WeReadApplication.getAppResources().getColor(R.color.colorTealPrimary);
            case Pink:
                return WeReadApplication.getAppResources().getColor(R.color.colorPinkPrimary);
            case DeepPurple:
                return WeReadApplication.getAppResources().getColor(R.color.colorDeepPurplePrimary);
            case Orange:
                return WeReadApplication.getAppResources().getColor(R.color.colorOrangePrimary);
            case Indigo:
                return WeReadApplication.getAppResources().getColor(R.color.colorIndigoPrimary);
            case LightGreen:
                return WeReadApplication.getAppResources().getColor(R.color.colorLightGreenPrimary);
            case Lime:
                return WeReadApplication.getAppResources().getColor(R.color.colorLimePrimary);
            case DeepOrange:
                return WeReadApplication.getAppResources().getColor(R.color.colorDeepOrangePrimary);
            case Cyan:
                return WeReadApplication.getAppResources().getColor(R.color.colorCyanPrimary);
            case BlueGrey:
                return WeReadApplication.getAppResources().getColor(R.color.colorBlueGreyPrimary);

        }
        return WeReadApplication.getAppResources().getColor(R.color.colorCyanPrimary);
    }

    /**
     * 获取主题颜色（color）
     *
     * @return
     */
    public static int getThemeColorId() {
        Theme theme = SharedPreUtils.getInstance().getCurrentTheme();

        switch (theme) {
            case Blue:
                return R.color.colorBluePrimary;
            case Red:
                return R.color.colorRedPrimary;
            case Brown:
                return R.color.colorBrownPrimary;
            case Green:
                return R.color.colorGreenPrimary;
            case Purple:
                return R.color.colorPurplePrimary;
            case Teal:
                return R.color.colorTealPrimary;
            case Pink:
                return R.color.colorPinkPrimary;
            case DeepPurple:
                return R.color.colorDeepPurplePrimary;
            case Orange:
                return R.color.colorOrangePrimary;
            case Indigo:
                return R.color.colorIndigoPrimary;
            case LightGreen:
                return R.color.colorLightGreenPrimary;
            case Lime:
                return R.color.colorLimePrimary;
            case DeepOrange:
                return R.color.colorDeepOrangePrimary;
            case Cyan:
                return R.color.colorCyanPrimary;
            case BlueGrey:
                return R.color.colorBlueGreyPrimary;

        }
        return R.color.colorCyanPrimary;
    }
}
