package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.api.BookService;
import com.hsf1002.sky.weread.model.BookChaptersBean;
import com.hsf1002.sky.weread.model.ChapterContentBean;
import com.hsf1002.sky.weread.utils.BookManager;
import com.hsf1002.sky.weread.utils.BookSaveUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.activity.IBookChapters;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;
import com.hsf1002.sky.weread.widget.page.TxtChapter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hefeng on 18-5-18.
 */

public class VMBookContentInfo extends BaseViewModel {
    IBookChapters iBookChapters;

    Disposable disposable;
    String title;

    public VMBookContentInfo(Context context, IBookChapters iBookChapters) {
        super(context);
        this.iBookChapters = iBookChapters;
    }


    public void loadChapters(String bookid)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookChapters(bookid)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookChaptersBean>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(BookChaptersBean data) {
                        if (iBookChapters != null)
                        {
                            iBookChapters.bookChapters(data);
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });

    }

    public void loadContent(String bookid, List<TxtChapter> txtChapterList)
    {
        int size = txtChapterList.size();

        if (disposable != null)
        {
            disposable.dispose();
        }

        List<Observable<ChapterContentBean>> chapterContentBeans = new ArrayList<>(txtChapterList.size());
        ArrayDeque<String> titles = new ArrayDeque<>(txtChapterList.size());

        for (int i=0; i<size; ++i)
        {
            TxtChapter txtChapter = txtChapterList.get(i);
            if (!(BookManager.isChapterCached(bookid, txtChapter.getTitle())))
            {
                Observable<ChapterContentBean> contentBeanObservable = RxHttpUtils
                        .createApi(BookService.class)
                        .bookContent(txtChapter.getLink());
                chapterContentBeans.add(contentBeanObservable);
                titles.add(txtChapter.getTitle());
            }
            else if (i == 0)
            {
                if (iBookChapters != null)
                {
                    iBookChapters.finishChapters();
                }
            }
        }

        title = titles.poll();

        Observable.concat(chapterContentBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChapterContentBean>() {
                    @Override
                    public void accept(ChapterContentBean chapterContentBean) throws Exception {
                        BookSaveUtils.getInstance().saveChapterInfo(bookid, title, chapterContentBean.getChapter().getCpContent());
                        iBookChapters.finishChapters();
                        title = titles.poll();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (txtChapterList.get(0).getTitle().equals(title)) {
                            iBookChapters.errorChapters();
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable1) throws Exception {
                        disposable = disposable1;
                    }
                });
    }
}
