package com.hsf1002.sky.weread.viewmodel.fragment;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.api.BookService;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.fragment.IBookInfo;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-14.
 */

public class VMBooksInfo extends BaseViewModel {
    IBookInfo iBookInfo;

    public VMBooksInfo(Context context, IBookInfo iBookInfo) {
        super(context);
        this.iBookInfo = iBookInfo;
    }

    public void getBooks(String type, String major, int page)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(BookService.class)
                .books(type, major, page)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<List<BookBean>>() {
                    @Override
                    protected void onError(String errorMsg) {
                        if (iBookInfo != null)
                        {
                            iBookInfo.stopLoading();
                        }
                    }

                    @Override
                    protected void onSuccess(List<BookBean> data) {
                        if (iBookInfo != null)
                        {
                            iBookInfo.stopLoading();
                        }

                        if (data.size() > 0)
                        {
                            if (iBookInfo != null)
                            {
                                iBookInfo.getBooks(data, page > 1 ? true : false);
                            }
                        }
                        else
                        {
                            ToastUtils.show(R.string.no_more_books);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }
}
