package com.hsf1002.sky.weread.db.helper;

import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.db.gen.BookChapterBeanDao;
import com.hsf1002.sky.weread.db.gen.DaoSession;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by hefeng on 18-5-16.
 */

public class BookChapterHelper {

    private static volatile BookChapterHelper sInstance;
    private static DaoSession daoSession;
    private static BookChapterBeanDao bookChapterBeanDao;

    public static BookChapterHelper getInstance() {
        if (sInstance == null) {
            synchronized (BookChapterHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookChapterHelper();
                    daoSession = DaoDbHelper.getInstance().getDaoSession();
                    bookChapterBeanDao = daoSession.getBookChapterBeanDao();
                }
            }
        }
        return sInstance;
    }

    public void saveBookChaptersWithAsync(List<BookChapterBean> bookChapterBeans)
    {
        daoSession.startAsyncSession().runInTx(() ->
        {
           daoSession.getBookChapterBeanDao().insertOrReplaceInTx(bookChapterBeans);
        });
    }

    public void removeChapters(String book_id)
    {
        bookChapterBeanDao.queryBuilder().where(BookChapterBeanDao.Properties.BookId.eq(book_id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public Observable<List<BookChapterBean>> findBookChaptersInRx(String bookid)
    {
        return Observable.create(e -> {
           List<BookChapterBean> chapterBeans = daoSession.getBookChapterBeanDao()
                   .queryBuilder().where(BookChapterBeanDao.Properties.BookId.eq(bookid))
                   .list();
           e.onNext(chapterBeans);
        });
    }
}
