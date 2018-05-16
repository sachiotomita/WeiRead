package com.hsf1002.sky.weread.view.activity;

import com.hsf1002.sky.weread.model.AppUpdateBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

/**
 * Created by hefeng on 18-5-10.
 */

public interface ISetting extends IBaseLoadView{
    void appUpdate(AppUpdateBean appUpdateBean);
}
