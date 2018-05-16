package com.hsf1002.sky.weread.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.application.WeReadApplication;
import com.hsf1002.sky.weread.db.helper.UserHelper;
import com.hsf1002.sky.weread.model.AppUpdateBean;
import com.hsf1002.sky.weread.utils.AppUpdateUtils;
import com.hsf1002.sky.weread.utils.LoadingHelper;
import com.hsf1002.sky.weread.utils.SharedPreUtils;
import com.hsf1002.sky.weread.view.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hsf1002.sky.weread.constant.Constant.USERNAME;

/**
 * Created by hefeng on 18-5-11.
 */

public class SettingActivity extends BaseActivity implements ISetting {
    @BindView(R.id.btn_out)
    Button logoutBtn;

    @BindView(R.id.tv_version)
    TextView vertionTv;

    private VMSettingInfo model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new VMSettingInfo(this, this);
        setBiddingView(R.layout.activity_setting, NO_BINDING, model);
    }

    @Override
    protected void initView() {
        super.initView();

        initThemeToolBar(getString(R.string.setting));
        vertionTv.setText("version: " + WeReadApplication.packageInfo.versionName);
    }

    @OnClick({R.id.btn_out, R.id.rl_version})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_out:
                new MaterialDialog.Builder(this)
                        .title("exit log")
                        .content("exist login?")
                        .positiveText("sure")
                        .onPositive((dialog, which) ->
                        {
                            UserHelper.getInstance().removeAllUser();
                            SharedPreUtils.getInstance().sharedPreRemove(USERNAME);
                            finish();
                        })
                        .negativeText("cancel")
                        .onNegative((dialog, which) ->
                        {
                            dialog.dismiss();
                        })
                        .show();

                break;
            case R.id.rl_version:
                model.appUpdate(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading() {
        LoadingHelper.getInstance().showLoading(context);
    }

    @Override
    public void stopLoading() {
        LoadingHelper.getInstance().hideLoading();
    }

    @Override
    public void appUpdate(AppUpdateBean appUpdateBean) {
        AppUpdateUtils.getInstance().appUpdate(context, appUpdateBean);
    }
}
