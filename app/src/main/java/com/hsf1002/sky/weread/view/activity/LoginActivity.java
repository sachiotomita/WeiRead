package com.hsf1002.sky.weread.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMUserLoginInfo;
import com.hsf1002.sky.weread.widget.theme.ColorTextView;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hsf1002.sky.weread.constant.Constant.PASSWORD;
import static com.hsf1002.sky.weread.constant.Constant.USERNAME;

/**
 * Created by hefeng on 18-5-11.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.actv_username)
    AutoCompleteTextView username;

    @BindView(R.id.et_password)
    EditText password;

    @BindView(R.id.ctv_register)
    ColorTextView register;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private VMUserLoginInfo model;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new VMUserLoginInfo(this);
        setBiddingView(R.layout.activity_login, NO_BINDING, model);
    }

    @Override
    protected void initView() {
        super.initView();

        initThemeToolBar(getString(R.string.title_activity_login));
    }

    @OnClick({R.id.ctv_register, R.id.fab})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ctv_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 10000);
                break;
            case R.id.fab:
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();

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
                model.login(usernameText, passwordText);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 10000 && data != null)
        {
            String usernameText = data.getStringExtra(USERNAME);
            String passwordText = data.getStringExtra(PASSWORD);
            username.setText(usernameText == null ? "" : usernameText);
            password.setText(passwordText == null ? "" : passwordText);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
