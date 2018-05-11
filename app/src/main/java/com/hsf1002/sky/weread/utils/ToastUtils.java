package com.hsf1002.sky.weread.utils;

import android.content.Context;
import android.widget.Toast;

import com.hsf1002.sky.weread.application.WeReadApplication;

/**
 * Created by hefeng on 18-5-11.
 */

public class ToastUtils {
    private static Context context = WeReadApplication.getAppContext();
    private static Toast toast;

    public static void show(int resId)
    {
        show(context.getResources().getString(resId));
    }

    public static void show(CharSequence text)
    {
        if (toast != null)
        {
            toast.cancel();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
