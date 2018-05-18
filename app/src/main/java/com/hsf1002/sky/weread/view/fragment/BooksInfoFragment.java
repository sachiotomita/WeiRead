package com.hsf1002.sky.weread.view.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.view.activity.BookDetailActivity;
import com.hsf1002.sky.weread.view.adapter.BookInfoAdapter;
import com.hsf1002.sky.weread.view.base.BaseFragment;
import com.hsf1002.sky.weread.viewmodel.fragment.VMBooksInfo;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hsf1002.sky.weread.constant.Constant.BOOKID;
import static com.hsf1002.sky.weread.constant.Constant.BOOKIMAGE;
import static com.hsf1002.sky.weread.constant.Constant.GENDER;
import static com.hsf1002.sky.weread.constant.Constant.TITLE_NAME;
import static com.hsf1002.sky.weread.constant.Constant.TYPE;

/**
 * Created by hefeng on 18-5-14.
 */

public class BooksInfoFragment extends BaseFragment implements IBookInfo{

    @BindView(R.id.rv_bookinfo)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    SmartRefreshLayout smartRefreshLayout;
    //@BindView(R.id.swipe_refresh)
    //SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.loadinglayout)
    LoadingLayout loadingLayout;

    private String title;
    private String gender;
    private String type;
    private VMBooksInfo model;
    private List<BookBean> bookBeansList = new ArrayList<>();
    private BookInfoAdapter bookInfoAdapter;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = new VMBooksInfo(context, this);
        View view = setContentView(container, R.layout.fragment_book_info, model);

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public static BooksInfoFragment newInstance(String title, String gender, String type)
    {
        BooksInfoFragment fragment = new BooksInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_NAME, title);
        bundle.putString(GENDER, gender);
        bundle.putString(TYPE, type);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void initView() {
        super.initView();

        title = getArguments().getString(TITLE_NAME);
        gender = getArguments().getString(GENDER);
        type = getArguments().getString(TYPE);

        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                ++page;
                model.getBooks(type, title, page);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                model.getBooks(type, title, page);
            }
        });

        smartRefreshLayout.autoRefresh();
/*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
            {
                @Override
                public void onRefresh() {
                    page = 1;
                    model.getBooks(type, title, page);
                }
            }
        );
*/
        loadingLayout.setOnReloadListener( v -> model.getBooks(type, title, 1));

        bookInfoAdapter = new BookInfoAdapter(bookBeansList);
        bookInfoAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(bookInfoAdapter);

        bookInfoAdapter.setOnItemClickListener((adapter, view, position) ->
        {
            Intent intent = new Intent();
            intent.setClass(context, BookDetailActivity.class);
            intent.putExtra(BOOKID, bookBeansList.get(position).get_id());

            if (Build.VERSION.SDK_INT > 20)
            {
                ImageView imageView = view.findViewById(R.id.book_brief_iv_portrait);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, BOOKIMAGE).toBundle());
            }
            else
            {
                startActivity(intent);
            }
        });

       // model.getBooks(type, title, page);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void emptyData() {
        loadingLayout.setStatus(LoadingLayout.Empty);
    }

    @Override
    public void stopLoading() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void errorData(String error) {
        loadingLayout.setEmptyText(error).setStatus(LoadingLayout.Error);
    }

    @Override
    public void networkError() {
        loadingLayout.setStatus(LoadingLayout.No_Network);
    }

    @Override
    public void getBooks(List<BookBean> bookBeans, boolean isLoadMore) {
        if (!isLoadMore)
        {
            bookBeansList.clear();
        }

        bookBeansList.addAll(bookBeans);
        bookInfoAdapter.notifyDataSetChanged();
    }
}
