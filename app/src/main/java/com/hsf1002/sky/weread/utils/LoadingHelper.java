package com.hsf1002.sky.weread.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import com.github.ybq.android.spinkit.SpinKitView;
import com.hsf1002.sky.weread.R;

/**
 * Created by hefeng on 18-5-14.
 */

public class LoadingHelper {
    private static Dialog dialog = null;
    private static LoadingHelper loadingHelper = null;
    private SpinKitView mSpinKitView;
    Context context;


    public static LoadingHelper getInstance() {
        if (loadingHelper == null) {
            loadingHelper = new LoadingHelper();
        }
        return loadingHelper;
    }

    public void showLoading(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSpinKitView = dialog.findViewById(R.id.spin_kit);
        mSpinKitView.setColor(ThemeUtils.getThemeColor());
        dialog.show();
    }

    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
