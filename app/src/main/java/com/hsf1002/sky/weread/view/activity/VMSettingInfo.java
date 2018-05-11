package com.hsf1002.sky.weread.view.activity;

import android.content.Context;

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

    }
}
