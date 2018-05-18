package com.hsf1002.sky.weread.viewmodel.activity;

import android.content.Context;

import com.allen.library.RxHttpUtils;
import com.allen.library.interceptor.Transformer;
import com.hsf1002.sky.weread.api.BookService;
import com.hsf1002.sky.weread.api.UserService;
import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.model.BookChaptersBean;
import com.hsf1002.sky.weread.model.DeleteBookBean;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.activity.IBookDetail;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-17.
 */

public class VMBookDetailInfo extends BaseViewModel{
    IBookDetail iBookDetail;

    public VMBookDetailInfo(Context context, IBookDetail iBookDetail) {
        super(context);
        this.iBookDetail = iBookDetail;
    }

    public void bookinfo(String bookid)
    {
        iBookDetail.showLoading();

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookInfo(bookid)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iBookDetail.stopLoading();
                    }

                    @Override
                    protected void onSuccess(BookBean data) {
                        iBookDetail.stopLoading();
                        iBookDetail.getBookInfo(data);
                    }
                });
    }


    public void addBookShelf(CollBookBean collBookBean)
    {
        iBookDetail.showLoading();

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(BookService.class)
                .bookChapters(collBookBean.get_id())
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<BookChaptersBean>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iBookDetail.stopLoading();
                    }

                    @Override
                    protected void onSuccess(BookChaptersBean data) {
                        iBookDetail.stopLoading();

                        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
                        for (BookChaptersBean.ChatpterBean bean:data.getChapters())
                        {
                            BookChapterBean chapterBean = new BookChapterBean();
                            chapterBean.setBookId(data.getBook());
                            chapterBean.setLink(bean.getLink());
                            chapterBean.setTitle(bean.getTitle());
                            chapterBean.setUnreadble(bean.isRead());
                            bookChapterBeans.add(chapterBean);
                        }

                        collBookBean.setBookChapters(bookChapterBeans);
                        CollBookHelper.getInstance().saveBookWithAsync(collBookBean);

                        addBookShelfToServer(collBookBean.get_id());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }
                });
    }

    public void addBookShelfToServer(String bookid)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .addBookShelf(bookid)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(data);
                    }
                });
    }

    public void deleteBookShelfToServer(CollBookBean collBookBean)
    {
        DeleteBookBean bookBean = new DeleteBookBean();
        bookBean.setBookid(collBookBean.get_id());

        RxHttpUtils.getSInstance().addHeaders(tokenMap())
                .createSApi(UserService.class)
                .deleteBookShelf(bookBean)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(String data) {
                        ToastUtils.show(data);
                        CollBookHelper.getInstance().removeBookInRx(collBookBean);
                    }
                });
    }

}
