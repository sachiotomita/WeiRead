package com.hsf1002.sky.weread.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.fragment.IBookSearchInfo;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.view.adapter.BookInfoAdapter;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMBookSearchInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hsf1002.sky.weread.constant.Constant.BOOKID;

/**
 * Created by hefeng on 18-5-10.
 */

public class BookSearchActivity extends BaseActivity implements IBookSearchInfo {
    @BindView(R.id.iv_back)
    ImageView backIv;

    @BindView(R.id.rv_search)
    RecyclerView recyclerView;

    @BindView(R.id.iv_clear)
    ImageView clearIv;

    @BindView(R.id.et_search)
    EditText searchEt;

    private VMBookSearchInfo model;
    private List<BookBean> beanList = new ArrayList<>();
    private BookInfoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new VMBookSearchInfo(context, this);
        setBiddingView(R.layout.activity_book_search, NO_BINDING, model);
    }

    @Override
    protected void initView() {
        super.initView();

        adapter = new BookInfoAdapter(beanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) ->
        {
            Intent intent = new Intent();
            intent.setClass(context, BookDetailActivity.class);
            intent.putExtra(BOOKID, beanList.get(position).get_id());

            if (Build.VERSION.SDK_INT > 20)
            {
                ImageView imageView = view.findViewById(R.id.book_brief_iv_portrait);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, imageView, "bookimage").toBundle());
            }
            else
            {
                startActivity(intent);
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s))
                {
                    model.searchBooks(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void getSearchBooks(List<BookBean> bookBeans) {
        beanList.clear();
        beanList.addAll(bookBeans);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_back, R.id.iv_clear})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                searchEt.setText("");
                break;
            default:
                break;
        }
    }
}
