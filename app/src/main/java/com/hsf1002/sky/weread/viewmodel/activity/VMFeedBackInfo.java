package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.api.UserService;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.activity.IFeedBack;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

/**
 * Created by hefeng on 18-5-16.
 */

public class VMFeedBackInfo extends BaseViewModel {
    private IFeedBack iFeedBack;

    public VMFeedBackInfo(Context context, IFeedBack iFeedBack) {
        super(context);
        this.iFeedBack = iFeedBack;
    }

    public void commitFeedBack(String qq, String feedback)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(UserService.class)
                .userFeddBack(qq, feedback)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(R.string.feedback_success);
                        iFeedBack.feedbackSuccess();
                    }
                });
    }
}
