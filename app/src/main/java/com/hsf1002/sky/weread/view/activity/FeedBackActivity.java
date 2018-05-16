package com.hsf1002.sky.weread.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.utils.BaseUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMFeedBackInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hefeng on 18-5-11.
 */

public class FeedBackActivity extends BaseActivity implements IFeedBack{
    @BindView(R.id.et_qq)
    EditText qqEdit;

    @BindView(R.id.et_feedback)
    EditText feedbackEdit;

    private VMFeedBackInfo model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new VMFeedBackInfo(this, this);
        setBiddingView(R.layout.activity_feed_back, NO_BINDING, model);
        initThemeToolBar(getString(R.string.feedback));
    }

    @OnClick(R.id.btn_commit)
    public void onClick()
    {
        String qqStr = qqEdit.getText().toString();
        String feedbackStr = feedbackEdit.getText().toString();

        if (TextUtils.isEmpty(qqStr))
        {
            ToastUtils.show(R.string.qq_cannot_empty);
            return;
        }

        if (TextUtils.isEmpty(feedbackStr))
        {
            ToastUtils.show(R.string.feedback_cannot_empty);
            return;
        }
        model.commitFeedBack(qqStr, feedbackStr);
        BaseUtils.hideInput(feedbackEdit);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    public void feedbackSuccess() {
        qqEdit.setText("");
        feedbackEdit.setText("");
    }
}
