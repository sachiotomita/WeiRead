package com.hsf1002.sky.weread.viewmodel.fragment;

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
import com.hsf1002.sky.weread.utils.LoadingHelper;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxObserver;
import com.hsf1002.sky.weread.view.fragment.IBookShelf;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-16.
 */

public class VMBookShelf extends BaseViewModel {
    IBookShelf iBookShelf;

    public VMBookShelf(Context context, IBookShelf iBookShelf) {
        super(context);
        this.iBookShelf = iBookShelf;
    }

    public void deleteBookShelfFromServer(CollBookBean collBookBean)
    {
        LoadingHelper.getInstance().showLoading(context);

        DeleteBookBean deleteBookBean = new DeleteBookBean();
        deleteBookBean.setBookid(collBookBean.get_id());

        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(UserService.class)
                .deleteBookShelf(deleteBookBean)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<String>() {
                    @Override
                    protected void onError(String errorMsg) {
                        LoadingHelper.getInstance().hideLoading();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        LoadingHelper.getInstance().hideLoading();
                        ToastUtils.show(data);

                        CollBookHelper.getInstance().removeBookInRx(collBookBean);
                        iBookShelf.deleteSuccess();
                    }
                });
    }

    public void getBookShelf(List<CollBookBean> bookBeans)
    {
        RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(UserService.class)
                .getBookShelf()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver<List<BookBean>>() {
                    @Override
                    protected void onError(String errorMsg) {
                        iBookShelf.stopLoading();
                    }

                    @Override
                    protected void onSuccess(List<BookBean> data) {
                        iBookShelf.stopLoading();
                        List<CollBookBean> beans = new ArrayList<>();

                        for (BookBean bookBean : data)
                        {
                            beans.add(bookBean.getCollBookBean());

                            for (CollBookBean collBookBean:bookBeans)
                            {
                                if (bookBean.get_id().equals(collBookBean.get_id()))
                                {
                                    beans.remove(bookBean.getCollBookBean());
                                }
                            }
                        }
                        iBookShelf.bookShelfInfo(beans);
                    }
                });
    }

    public void setBookInfo(CollBookBean collBookBean)
    {
        LoadingHelper.getInstance().showLoading(context);
        if (CollBookHelper.getInstance().findBookById(collBookBean.get_id()) == null) {
            RxHttpUtils.getSInstance().addHeaders(tokenMap()).createSApi(BookService.class)
                    .bookChapters(collBookBean.get_id())
                    .compose(Transformer.switchSchedulers())
                    .subscribe(new RxObserver<BookChaptersBean>() {
                        @Override
                        protected void onError(String errorMsg) {
                            LoadingHelper.getInstance().hideLoading();
                        }

                        @Override
                        protected void onSuccess(BookChaptersBean data) {
                            LoadingHelper.getInstance().hideLoading();
                            List<BookChapterBean> bookChapterList = new ArrayList<>();
                            for (BookChaptersBean.ChatpterBean bean : data.getChapters()) {
                                BookChapterBean chapterBean = new BookChapterBean();
                                chapterBean.setBookId(data.getBook());
                                chapterBean.setLink(bean.getLink());
                                chapterBean.setTitle(bean.getTitle());
//                                chapterBean.setTaskName("下载");
                                chapterBean.setUnreadble(bean.isRead());
                                bookChapterList.add(chapterBean);
                            }
                            collBookBean.setBookChapters(bookChapterList);
                            CollBookHelper.getInstance().saveBookWithAsync(collBookBean);
                            iBookShelf.bookInfo(collBookBean);
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }
                    });
        } else {
            LoadingHelper.getInstance().hideLoading();
            iBookShelf.bookInfo(collBookBean);
        }


    }

}
