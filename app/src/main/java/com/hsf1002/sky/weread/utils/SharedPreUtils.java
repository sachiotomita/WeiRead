package com.hsf1002.sky.weread.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hsf1002.sky.weread.application.WeReadApplication;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.widget.theme.Theme;

/**
 * Created by hefeng on 18-5-9.
 */

public class SharedPreUtils {
    private static final String SHARED_NAME = "WeRead_SP";
    private static SharedPreUtils sInstance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public SharedPreUtils() {
        sharedPreferences = WeReadApplication.getAppContext().getSharedPreferences(SHARED_NAME, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();
    }

    public static SharedPreUtils getInstance()
    {
        if (sInstance == null)
        {
            synchronized (SharedPreUtils.class)
            {
                if (sInstance == null)
                {
                    sInstance = new SharedPreUtils();
                }
            }
        }

        return  sInstance;
    }

    public void sharedPreRemove(String key) {
        editor.remove(key).apply();
    }

    public int getInt(String key, int value)
    {
        return sharedPreferences.getInt(key, value);
    }

    public void putI(String key, int value)
    {
        editor.putInt(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key, Boolean value)
    {
        return sharedPreferences.getBoolean(key, value);
    }

    public void putBoolean(String key, Boolean value)
    {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getString(String key, String value)
    {
        return sharedPreferences.getString(key, value);
    }

    public void putString(String key, String value)
    {
        editor.putString(key, value);
        editor.apply();
    }

    public Theme getCurrentTheme()
    {
        return Theme.valueOf(getString(Constant.APP_THEME, Theme.Cyan.name()));
    }

    public void setCurrentTheme(Theme currentTheme)
    {
        putString(Constant.APP_THEME, currentTheme.name());
    }
}
