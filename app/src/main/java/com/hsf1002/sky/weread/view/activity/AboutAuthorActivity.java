package com.hsf1002.sky.weread.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import butterknife.BindView;

/**
 * Created by hefeng on 18-5-11.
 */

public class AboutAuthorActivity extends BaseActivity {
    @BindView(R.id.iv_avatar)
    ImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBiddingView(R.layout.activity_about_mine, NO_BINDING, new BaseViewModel(this));
        initThemeToolBar(getString((R.string.about)));

        Glide.with(context).load(R.mipmap.avatar).apply(new RequestOptions().transform(new CircleCrop())).into(avatar);
    }
}
