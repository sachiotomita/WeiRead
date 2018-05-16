package com.hsf1002.sky.weread.view.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.api.UserService;
import com.hsf1002.sky.weread.application.WeReadApplication;
import com.hsf1002.sky.weread.model.AppUpdateBean;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

/**
 * Created by hefeng on 18-5-10.
 */

public class VMSettingInfo extends BaseViewModel {
    ISetting iSetting;

    public VMSettingInfo(Context context) {
        super(context);
    }

    public VMSettingInfo(Context context, ISetting iSetting) {
        super(context);
        this.iSetting = iSetting;
    }

    public void appUpdate(boolean isTip)
    {
        iSetting.showLoading();

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .appUpdate()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<AppUpdateBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iSetting.stopLoading();
                    }

                    @Override
                    protected void onSuccess(AppUpdateBean data) {
                        iSetting.stopLoading();

                        if (WeReadApplication.packageInfo.versionCode < data.getVersionCode())
                        {
                            iSetting.appUpdate(data);
                        }
                        else
                        {
                            if (isTip)
                            {
                                ToastUtils.show(R.string.current_version_is_newest);
                            }
                        }
                    }
                });
    }
}
