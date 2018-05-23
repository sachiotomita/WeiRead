package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.api.UserService;
import com.hsf1002.sky.weread.db.entity.UserBean;
import com.hsf1002.sky.weread.db.helper.UserHelper;
import com.hsf1002.sky.weread.utils.SharedPreUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.activity.IUserInfo;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.hsf1002.sky.weread.constant.Constant.USERNAME;

/**
 * Created by hefeng on 18-5-22.
 */

public class VMUserInfo extends BaseViewModel {
    IUserInfo iUserInfo;

    public VMUserInfo(Context context, IUserInfo iUserInfo) {
        super(context);
        this.iUserInfo = iUserInfo;
    }

    public void getUserInfo()
    {
        iUserInfo.showLoading();

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .getUserInfo()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<UserBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(UserBean data) {
                        iUserInfo.stopLoading();
                        iUserInfo.userInfo(data);
                    }
                });
    }

    public void uploadAvatar(String path)
    {
        iUserInfo.showLoading();
        String username = SharedPreUtils.getInstance().getString(USERNAME, "");
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .avatar(body)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        iUserInfo.stopLoading();
                        iUserInfo.uploadSuccess(data);
                        UserBean userBean = UserHelper.getInstance().findUserByName(username);
                        userBean.setIcon(data);
                        UserHelper.getInstance().updateUser(userBean);
                    }
                });

    }

    public void updatePassword(String password)
    {
        iUserInfo.showLoading();

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .updatePassword(password)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        iUserInfo.stopLoading();
                        ToastUtils.show(data);
                    }
                });
    }

    public void updateUserInfo(String nickname, String brief)
    {
        iUserInfo.showLoading();

        String username = SharedPreUtils.getInstance().getString(USERNAME, "");
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .updateUserInfo(nickname, brief)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iUserInfo.stopLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        iUserInfo.stopLoading();
                        ToastUtils.show(data);
                        UserBean userBean = UserHelper.getInstance().findUserByName(username);
                        userBean.setBrief(brief);
                        userBean.setNickname(nickname);
                        UserHelper.getInstance().updateUser(userBean);
                    }
                });
    }

}
