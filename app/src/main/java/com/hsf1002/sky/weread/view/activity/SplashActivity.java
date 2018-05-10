package com.hsf1002.sky.weread.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.view.base.BaseActivity;

import static com.hsf1002.sky.weread.constant.Constant.SPLASH_ACTIVITY_DURATION;

/**
 * Created by hefeng on 18-5-9.
 */

public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed( () ->
        {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_ACTIVITY_DURATION);
    }
}
