package com.hsf1002.sky.weread.view.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-11.
 */

public class BaseFragment extends Fragment {

    protected BaseViewModel model;
    protected Context context;
    private View bindView;
    private View view;
    protected CompositeDisposable compositeDisposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View setBinddingView(LayoutInflater inflater, ViewGroup container, int resId, int variable, BaseViewModel model)
    {
        if (bindView == null)
        {
            ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, resId, container, false);
            dataBinding.setVariable(variable, model);
            bindView = dataBinding.getRoot();
            ButterKnife.bind(this, bindView);
            this.model = model;
        }
        return bindView;
    }

    public View setContentView(ViewGroup container, int resId, BaseViewModel model)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getActivity()).inflate(resId, container, false);
            ButterKnife.bind(this, view);
            this.model = model;
            initView();
        }

        return view;
    }

    public void initView()
    {

    }

    protected void addDisposable(Disposable disposable)
    {
        if (compositeDisposable == null)
        {
            compositeDisposable = new CompositeDisposable();
        }

        compositeDisposable.add(disposable);
    }

    public void startActivity(Class<?> className) {
        Intent intent = new Intent(context, className);
        startActivity(intent);
    }

    public void startActivity(Class<?> className, Bundle bundle) {
        Intent intent = new Intent(context, className);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (model != null)
        {
            model.onDestroy();
        }
    }
}
