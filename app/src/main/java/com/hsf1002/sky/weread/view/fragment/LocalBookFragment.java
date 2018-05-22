package com.hsf1002.sky.weread.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.model.LocalFileBean;
import com.hsf1002.sky.weread.utils.FileUtils;
import com.hsf1002.sky.weread.utils.LoadingHelper;
import com.hsf1002.sky.weread.utils.rxhelper.RxUtils;
import com.hsf1002.sky.weread.view.adapter.LocalFileAdapter;
import com.hsf1002.sky.weread.view.base.BaseFileFragment;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;
import com.hsf1002.sky.weread.widget.DividerItemDecoration;
import com.hsf1002.sky.weread.widget.theme.ColorButton;
import com.weavey.loading.lib.LoadingLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hefeng on 18-5-21.
 */

public class LocalBookFragment extends BaseFileFragment {
    @BindView(R.id.btn_scan)
    ColorButton scanBtn;

    @BindView(R.id.rv_files)
    RecyclerView recyclerView;

    @BindView(R.id.loadlayout)
    LoadingLayout loadingLayout;

    private List<LocalFileBean> fileBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_local_book, new BaseViewModel(context));

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public static LocalBookFragment newInstance()
    {
        LocalBookFragment fragment = new LocalBookFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        mAdapter = new LocalFileAdapter(fileBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context));
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter1, view, position) ->
        {
            String id = fileBeanList.get(position).getFile().getAbsolutePath();

            if (CollBookHelper.getInstance().findBookById(id) != null)
            {
                return;
            }

            mAdapter.setCheckedItem(position);

            if (mListener != null)
            {
                mListener.onItemCheckedChange(mAdapter.getItemIsChecked(position));
            }
        });

        scanBtn.setOnClickListener( v ->
        {
            scanFiles();
        });
    }

    private void scanFiles()
    {
        LoadingHelper.getInstance().showLoading(context);

        addDisposable(FileUtils.getSDTxtFile().compose(RxUtils::toSimpleSingle)
            .subscribe(files -> {
                LoadingHelper.getInstance().hideLoading();
                fileBeanList.clear();

                if (files.size() == 0)
                {
                    loadingLayout.setStatus(LoadingLayout.Empty);
                }
                else
                {
                    loadingLayout.setStatus(LoadingLayout.Success);

                    for (File file:files)
                    {
                        LocalFileBean localFileBean = new LocalFileBean();
                        localFileBean.setSelect(false);
                        localFileBean.setFile(file);
                        fileBeanList.add(localFileBean);
                    }

                    mAdapter.notifyDataSetChanged();

                    if (mListener != null)
                    {
                        mListener.onCategoryChanged();
                    }
                }
            }));
    }
}
