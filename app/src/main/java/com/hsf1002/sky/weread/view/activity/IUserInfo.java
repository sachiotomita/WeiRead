package com.hsf1002.sky.weread.view.activity;

import com.hsf1002.sky.weread.db.entity.UserBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

/**
 * Created by hefeng on 18-5-22.
 */

public interface IUserInfo extends IBaseLoadView {
    void uploadSuccess(String imageUrl);
    void userInfo(UserBean userBean);
}
