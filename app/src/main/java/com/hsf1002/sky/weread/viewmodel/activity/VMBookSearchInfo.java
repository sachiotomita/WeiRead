package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.api.BookService;
import com.hsf1002.sky.weread.fragment.IBookSearchInfo;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-23.
 */

public class VMBookSearchInfo extends BaseViewModel {
    IBookSearchInfo iBookSearchInfo;

    public VMBookSearchInfo(Context context, IBookSearchInfo iBookSearchInfo) {
        super(context);
        this.iBookSearchInfo = iBookSearchInfo;
    }

    public void searchBooks(String key)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .booksSearch(key)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<List<BookBean>>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(List<BookBean> data) {
                        if (iBookSearchInfo != null)
                        {
                            iBookSearchInfo.getSearchBooks(data);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }
}
