package com.hsf1002.sky.weread.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.model.BookClassifyBean;
import com.hsf1002.sky.weread.view.activity.BookListActivity;
import com.hsf1002.sky.weread.view.adapter.ClassifyAdapter;
import com.hsf1002.sky.weread.view.base.BaseFragment;
import com.hsf1002.sky.weread.viewmodel.fragment.VMBookClassify;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.hsf1002.sky.weread.constant.Constant.GENDER;
import static com.hsf1002.sky.weread.constant.Constant.TABINDEX;
import static com.hsf1002.sky.weread.constant.Constant.TABNAME;
import static com.hsf1002.sky.weread.constant.Constant.TITLE_NAME;

/**
 * Created by hefeng on 18-5-14.
 */

public class ClassifyFragment extends BaseFragment implements IClassifyBook {

    @BindView(R.id.rv_classify)
    RecyclerView recyclerView;

    @BindView(R.id.loadinglayout)
    LoadingLayout loadingLayout;

    private String name;
    private static int tabIndex;
    private String gender = "male";
    private ClassifyAdapter classifyAdapter;
    private VMBookClassify model;
    private List<BookClassifyBean.ClassifyBean> classifyBeansList = new ArrayList<>();

    public static ClassifyFragment newInstance(String name, int index)
    {
        Bundle args = new Bundle();
        args.putString(TABNAME, name);
        args.putInt(TABINDEX, index);
        ClassifyFragment fragment = new ClassifyFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = new VMBookClassify(context, this);
        View view = setContentView(container, R.layout.fragment_classify, model);
        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        name = getArguments().getString(TABNAME);
        tabIndex = getArguments().getInt(TABINDEX);
        model.bookClassify();

        classifyAdapter = new ClassifyAdapter(classifyBeansList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(classifyAdapter);
        classifyAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        classifyAdapter.setOnItemChildClickListener(((adapter, view, position) ->
        {
            Bundle bundle = new Bundle();
            bundle.putString(GENDER, gender);
            bundle.putString(TITLE_NAME, classifyBeansList.get(position).getName());
            startActivity(BookListActivity.class, bundle);
        }));
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
    public void getBookClassify(BookClassifyBean bookClassifyBean) {
        loadingLayout.setStatus(LoadingLayout.Success);

        classifyBeansList.clear();

        //switch (tabIndex)
        switch (name)
        {
            //case 0:
            case "boy":
                gender = "male";
                classifyBeansList.addAll(bookClassifyBean.getMale());
                break;
            //case 1:
            case "girl":
                gender = "female";
                classifyBeansList.addAll(bookClassifyBean.getFemale());
                break;
            //case 2:
            case "press":
                gender = "press";
                classifyBeansList.addAll(bookClassifyBean.getPress());
                break;
            default:
                break;
        }

        classifyAdapter.notifyDataSetChanged();
    }
}
