package com.hsf1002.sky.weread.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hsf1002.sky.weread.application.WeReadApplication;

/**
 * Created by hefeng on 18-5-11.
 */

public class BaseUtils {
    public static void setIconDrawable(TextView view, @DrawableRes int iconRes) {
        view.setCompoundDrawablesWithIntrinsicBounds(WeReadApplication.getAppResources().getDrawable(iconRes),
                null, null, null);
        view.setCompoundDrawablePadding(DimenUtils.dp2px(10));
    }

    public static void getAppDetailSettingIntent(Context context, String packageName) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        context.startActivity(intent);
    }

    public static String format1Digits(Object d) {
        return String.format("%.1f", Double.parseDouble(d+""));
    }

    public static void hideInput(final EditText et) {
        InputMethodManager imm = (InputMethodManager)
                et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }
}
