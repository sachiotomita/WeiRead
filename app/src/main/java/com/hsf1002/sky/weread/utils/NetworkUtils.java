package com.hsf1002.sky.weread.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hsf1002.sky.weread.application.WeReadApplication;

/**
 * Created by hefeng on 18-5-14.
 */

public class NetworkUtils {

    public static NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) WeReadApplication
                .getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }
    public static boolean isConnected() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isConnected();
    }
}
