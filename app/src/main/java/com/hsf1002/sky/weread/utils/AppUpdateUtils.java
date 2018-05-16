package com.hsf1002.sky.weread.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.RxHttpUtils;
import com.allen.library.download.DownloadObserver;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.model.AppUpdateBean;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-15.
 */

public class AppUpdateUtils {

    private static AppUpdateUtils sInstance;
    Disposable disposable;

    public static AppUpdateUtils getInstance()
    {
        if (sInstance == null)
        {
            synchronized (AppUpdateUtils.class)
            {
                if (sInstance == null)
                {
                    sInstance = new AppUpdateUtils();
                }
            }
        }

        return sInstance;
    }

    public void appUpdate(Context context, AppUpdateBean appUpdateBean) {
        new MaterialDialog.Builder(context)
                .title("version update")
                .content("update to new version?")
                .positiveText("update now")
                .onPositive((dialog, which) ->
                {
                    dialog.dismiss();
                    updateDownload(context, appUpdateBean.getDownloadUrl());
                })
                .negativeText("cancel")
                .onNegative((dialog, which) ->
                {
                    dialog.dismiss();
                })
                .show();
    }

    private void updateDownload(Context context, String downloadUrl)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.layout_app_update, false)
                .title("updating...")
                .negativeText("cancel")
                .onNegative((dialog1, which) ->
                {
                    if (disposable != null)
                    {
                        disposable.dispose();
                    }
                    ToastUtils.show("cancel update");
                    dialog1.dismiss();
                })
                .build();
        dialog.setOnDismissListener(dialog1 ->
        {
            if (disposable != null)
            {
                disposable.dispose();
            }
            ToastUtils.show("cancel update");
            dialog1.dismiss();
        });

        NumberProgressBar progressBar = dialog.getCustomView().findViewById(R.id.npb_download);
        dialog.show();

        String url = Constant.BASE_URL + downloadUrl;
        RxHttpUtils.downloadFile(url)
                .subscribe(new DownloadObserver("WeYue.apk") {
                    @Override
                    protected void getDisposable(Disposable disposable1) {
                        disposable = disposable1;
                    }

                    @Override
                    protected void onError(String s) {

                    }

                    @Override
                    protected void onSuccess(long l, long l1, float v, boolean b, String s) {
                        if (b)
                        {
                            dialog.dismiss();

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            {
                                Uri contentUri = FileProvider.getUriForFile(context, "com.hsf1002.sky.weread.utils.RongFileProvider", new File(s));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            }
                            else
                            {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.fromFile(new File(s)), "application/vnd.android.package-archive");
                            }
                            context.startActivity(intent);
                        }
                        progressBar.setProgress((int)v);
                    }
                });
    }
}
