package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.api.UserService;
import com.hsf1002.sky.weread.db.entity.UserBean;
import com.hsf1002.sky.weread.db.helper.UserHelper;
import com.hsf1002.sky.weread.utils.SharedPreUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

/**
 * Created by hefeng on 18-5-15.
 */

public class VMUserLoginInfo extends BaseViewModel {
    public VMUserLoginInfo(Context context) {
        super(context);
    }

    public void login(String username, String password)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .login(username, password)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(UserBean data) {
                        ToastUtils.show(R.string.login_success);
                        UserHelper.getInstance().saveUser(data);
                        SharedPreUtils.getInstance().putString("token", data.getToken());
                        SharedPreUtils.getInstance().putString("username", data.name);
                        ((BaseActivity)context).finish();
                    }
                });
    }
}
