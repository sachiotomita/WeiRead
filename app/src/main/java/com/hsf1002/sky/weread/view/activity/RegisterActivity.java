package com.hsf1002.sky.weread.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMUserRegisterInfo;

import butterknife.BindView;

import static com.hsf1002.sky.weread.constant.Constant.PASSWORD;
import static com.hsf1002.sky.weread.constant.Constant.USERNAME;

/**
 * Created by hefeng on 18-5-15.
 */

public class RegisterActivity extends BaseActivity implements IUserRegister {
    @BindView(R.id.actv_username)
    AutoCompleteTextView username;

    @BindView(R.id.et_password)
    EditText password;

    @BindView(R.id.et_password_confirm)
    EditText rePassword;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private VMUserRegisterInfo model;
    private String usernameText;
    private String passwordText;
    private String passwordText2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new VMUserRegisterInfo(this, this);
        setBiddingView(R.layout.activity_register, NO_BINDING, model);
    }

    @Override
    protected void initView() {
        super.initView();

        initThemeToolBar(getString(R.string.register));

        fab.setOnClickListener(v -> {
            usernameText = username.getText().toString();
            passwordText = password.getText().toString();
            passwordText2 = rePassword.getText().toString();

            if (TextUtils.isEmpty(usernameText))
            {
                ToastUtils.show(R.string.id_cannot_empty);
                return;
            }
            if (TextUtils.isEmpty(passwordText))
            {
                ToastUtils.show(R.string.password_cannot_empty);
                return;
            }
            if (!passwordText.equals(passwordText2))
            {
                ToastUtils.show(R.string.repassword_cannot_same);
                return;
            }
            model.register(usernameText, passwordText);
        });
    }

    @Override
    public void registerSuccess() {
        setResult(RESULT_OK, new Intent().putExtra(USERNAME, usernameText).putExtra(PASSWORD, passwordText));
        finish();
    }
}
