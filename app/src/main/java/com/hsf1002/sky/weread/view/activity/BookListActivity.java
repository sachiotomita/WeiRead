package com.hsf1002.sky.weread.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.view.adapter.BaseViewPageAdapter;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.view.fragment.BooksInfoFragment;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hsf1002.sky.weread.constant.Constant.GENDER;
import static com.hsf1002.sky.weread.constant.Constant.TITLE_NAME;

/**
 * Created by hefeng on 18-5-14.
 */

public class BookListActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabStrip)
    NavigationTabStrip navigationTabStrip;

    private String title;
    private String gender;

    private String[] titles = {"热门", "新书", "好评"/*, "完结"*/};
    private String[] types= {"hot", "new", "reputation", "over"};
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBiddingView(R.layout.activity_book_list, NO_BINDING, new BaseViewModel(context));
    }

    @Override
    protected void initView() {
        super.initView();

        title = getIntent().getStringExtra(TITLE_NAME);
        gender = getIntent().getStringExtra(GENDER);

        initThemeToolBar(title);

        fragments = new ArrayList<>();

        for (String type : types)
        {
            fragments.add(BooksInfoFragment.newInstance(title, gender, type));
        }

        viewPager.setAdapter(new BaseViewPageAdapter(getSupportFragmentManager(), titles, fragments));
        viewPager.setOffscreenPageLimit(4);
        navigationTabStrip.setTitles(titles);
        navigationTabStrip.setViewPager(viewPager);
    }
}
