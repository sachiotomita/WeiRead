package com.hsf1002.sky.weread.db.helper;

import com.hsf1002.sky.weread.db.entity.BookRecordBean;
import com.hsf1002.sky.weread.db.gen.BookRecordBeanDao;
import com.hsf1002.sky.weread.db.gen.DaoSession;

/**
 * Created by hefeng on 18-5-16.
 */

public class BookRecordHelper {
    private static volatile BookRecordHelper sInstance;
    private static DaoSession daoSession;
    private static BookRecordBeanDao bookRecordBeanDao;

    public static BookRecordHelper getInstance() {
        if (sInstance == null) {
            synchronized (BookRecordHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookRecordHelper();
                    daoSession = DaoDbHelper.getInstance().getDaoSession();
                    bookRecordBeanDao = daoSession.getBookRecordBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存阅读记录
     */
    public void saveRecordBook(BookRecordBean collBookBean) {
        bookRecordBeanDao.insertOrReplace(collBookBean);
    }

    /**
     * 删除书籍记录
     */
    public void removeBook(String bookId) {
        bookRecordBeanDao
                .queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }


    /**
     * 查询阅读记录
     */
    public BookRecordBean findBookRecordById(String bookId) {
        return bookRecordBeanDao.queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId)).unique();
    }

}
