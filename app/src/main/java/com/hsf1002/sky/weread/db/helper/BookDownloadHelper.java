package com.hsf1002.sky.weread.db.helper;

import com.hsf1002.sky.weread.db.gen.DaoSession;
import com.hsf1002.sky.weread.db.gen.DownloadTaskBeanDao;

/**
 * Created by hefeng on 18-5-16.
 */

public class BookDownloadHelper {

    private static volatile BookDownloadHelper sInstance;
    private static DaoSession daoSession;
    private static DownloadTaskBeanDao downloadTaskBeanDao;

    public static BookDownloadHelper getInstance() {
        if (sInstance == null) {
            synchronized (BookChapterHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookDownloadHelper();
                    daoSession = DaoDbHelper.getInstance().getDaoSession();
                    downloadTaskBeanDao = daoSession.getDownloadTaskBeanDao();
                }
            }
        }
        return sInstance;
    }

    public void removeDownloadTask(String book_id)
    {
        downloadTaskBeanDao.queryBuilder().where(DownloadTaskBeanDao.Properties.BookId.eq(book_id))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }
}
