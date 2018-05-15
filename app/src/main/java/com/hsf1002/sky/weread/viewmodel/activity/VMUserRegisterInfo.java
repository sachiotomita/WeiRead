package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.api.UserService;
import com.hsf1002.sky.weread.utils.MD5Utils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.activity.IUserRegister;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

/**
 * Created by hefeng on 18-5-15.
 */

public class VMUserRegisterInfo extends BaseViewModel {
    IUserRegister iUserRegister;

    public VMUserRegisterInfo(Context context, IUserRegister iUserRegister) {
        super(context);
        this.iUserRegister = iUserRegister;
    }

    public void register(String username, String password)
    {
        String md5Pass = MD5Utils.encrypt(password);

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .register(username, md5Pass)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(data);

                        if (data.equals("注册成功"))
                        {
                            iUserRegister.registerSuccess();
                        }
                    }
                });
    }
}
