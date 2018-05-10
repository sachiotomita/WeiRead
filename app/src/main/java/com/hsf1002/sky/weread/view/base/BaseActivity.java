package com.hsf1002.sky.weread.view.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.utils.SharedPreUtils;
import com.hsf1002.sky.weread.utils.SystemUtils;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;
import com.hsf1002.sky.weread.widget.theme.ColorView;
import com.hsf1002.sky.weread.widget.theme.Theme;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hefeng on 18-5-9.
 */

public class BaseActivity extends AppCompatActivity {
    protected static int NO_BINDING = -1;

    protected Context context;
    private Toolbar toolbar;
    private BaseViewModel model;
    private Unbinder unbinder;
    private ColorView statusBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            setTranslucentStatus(true);
        }

        onPreCreate();
    }

    public void setBiddingView(@LayoutRes int resId, int variable, BaseViewModel model)
    {
        if (variable == NO_BINDING)
        {
            setContentView(resId);
        }
        else
        {
            ViewDataBinding dataBinding = DataBindingUtil.setContentView(this, resId);
            dataBinding.setVariable(variable, model);
        }

        this.model = model;
        unbinder = ButterKnife.bind(this);
        context = this;
        initView();
    }

    protected void initView()
    {

    }

    // define toolbar with back icon
    public void initThemeToolBar(String title)
    {
        initStatusBar();
        AppCompatImageView toolbarMore = findViewById(R.id.iv_toolbar_more);
        AppCompatImageView toolbarBack = findViewById(R.id.iv_toolbar_back);
        TextView toobarTitle = findViewById(R.id.tv_toolbar_title);
        toobarTitle.setSelected(true);
        toobarTitle.setText(title);
        toolbarBack.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        toolbarBack.setOnClickListener( v ->
        {
            finish();
        });
        toolbarMore.setVisibility(View.GONE);
    }

    private void initStatusBar()
    {
        statusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = SystemUtils.getStatusHeight(this);
            statusBar.setLayoutParams(statusBar.getLayoutParams());
        }
        else
        {
            statusBar.setVisibility(View.GONE);
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    private void onPreCreate() {
        Theme theme = SharedPreUtils.getInstance().getCurrentTheme();
        switch (theme) {
            case Blue:
                setTheme(R.style.BlueTheme);
                break;
            case Red:
                setTheme(R.style.RedTheme);
                break;
            case Brown:
                setTheme(R.style.BrownTheme);
                break;
            case Green:
                setTheme(R.style.GreenTheme);
                break;
            case Purple:
                setTheme(R.style.PurpleTheme);
                break;
            case Teal:
                setTheme(R.style.TealTheme);
                break;
            case Pink:
                setTheme(R.style.PinkTheme);
                break;
            case DeepPurple:
                setTheme(R.style.DeepPurpleTheme);
                break;
            case Orange:
                setTheme(R.style.OrangeTheme);
                break;
            case Indigo:
                setTheme(R.style.IndigoTheme);
                break;
            case LightGreen:
                setTheme(R.style.LightGreenTheme);
                break;
            case Lime:
                setTheme(R.style.LimeTheme);
                break;
            case DeepOrange:
                setTheme(R.style.DeepOrangeTheme);
                break;
            case Cyan:
                setTheme(R.style.CyanTheme);
                break;
            case BlueGrey:
                setTheme(R.style.BlueGreyTheme);
                break;
            default:
                break;
        }
    }

    public void startActivity(Class<?> className)
    {
        Intent intent = new Intent(context, className);
        startActivity(intent);
    }

    public void startActivity(Class<?> className, Bundle bundle)
    {
        Intent intent = new Intent(context, className);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (model != null)
        {
            model.onDestroy();
        }

        if (unbinder != null)
        {
            unbinder.unbind();
        }
    }
}
