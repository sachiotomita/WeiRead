package com.hsf1002.sky.weread.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.application.WeReadApplication;
import com.hsf1002.sky.weread.view.activity.MainActivity;
import com.hsf1002.sky.weread.view.adapter.BaseViewPageAdapter;
import com.hsf1002.sky.weread.view.base.BaseFragment;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hefeng on 18-5-11.
 */

public class BookClassifyFragment extends BaseFragment{

    @BindView(R.id.nts_classify)
    NavigationTabStrip navigationTabStrip;

    @BindView(R.id.vp_classify)
    ViewPager viewPager;

    private String titles[] = WeReadApplication.getAppContext().getResources().getStringArray(R.array.classify_pager_name);
    private List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_book_classify, new BaseViewModel(context));
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public static BookClassifyFragment newInstance()
    {
        BookClassifyFragment fragment = new BookClassifyFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();

        for (int i=0; i< titles.length; ++i)
        {
            fragments.add(ClassifyFragment.newInstance(titles[i], navigationTabStrip.getTabIndex()));
        }

        viewPager.setAdapter(new BaseViewPageAdapter(getActivity().getSupportFragmentManager(), titles, fragments));
        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((MainActivity)getActivity()).setLeftSlide(position == 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOffscreenPageLimit(4);
        navigationTabStrip.setTitles(titles);
        navigationTabStrip.setViewPager(viewPager);
    }
}
