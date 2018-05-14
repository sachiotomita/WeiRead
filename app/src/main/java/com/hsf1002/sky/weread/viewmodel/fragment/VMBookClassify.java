package com.hsf1002.sky.weread.viewmodel.fragment;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.api.BookService;
import com.hsf1002.sky.weread.model.BookClassifyBean;
import com.hsf1002.sky.weread.utils.NetworkUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.fragment.IClassifyBook;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import io.reactivex.disposables.Disposable;


/**
 * Created by hefeng on 18-5-14.
 */

public class VMBookClassify extends BaseViewModel {

    IClassifyBook iClassifyBook;

    public VMBookClassify(Context context, IClassifyBook iClassifyBook) {
        super(context);
        this.iClassifyBook = iClassifyBook;
    }

    public void bookClassify()
    {
        if (!NetworkUtils.isConnected())
        {
            if (iClassifyBook != null)
            {
                iClassifyBook.networkError();
            }

            return;
        }

        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(BookService.class)
                .bookClassify()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookClassifyBean>() {

                    @Override
                    protected void onError(String errorMsg) {
                        if (iClassifyBook != null)
                        {
                            iClassifyBook.stopLoading();
                            iClassifyBook.errorData(errorMsg);
                        }
                    }

                    @Override
                    protected void onSuccess(BookClassifyBean data) {
                        if (iClassifyBook != null)
                        {
                            iClassifyBook.stopLoading();

                            if (data == null)
                            {
                                iClassifyBook.emptyData();
                                return;
                            }
                            iClassifyBook.getBookClassify(data);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }
}
