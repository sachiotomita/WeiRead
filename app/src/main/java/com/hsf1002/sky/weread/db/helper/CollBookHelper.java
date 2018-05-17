package com.hsf1002.sky.weread.db.helper;

import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.db.gen.CollBookBeanDao;
import com.hsf1002.sky.weread.db.gen.DaoSession;
import com.hsf1002.sky.weread.utils.FileUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by hefeng on 18-5-16.
 */

public class CollBookHelper {
    private static volatile  CollBookHelper sInstance;
    private static DaoSession daoSession;
    private static CollBookBeanDao collBookBeanDao;


    public static CollBookHelper getInstance()
    {
        if (sInstance == null)
        {
            synchronized (CollBookHelper.class)
            {
                if (sInstance == null)
                {
                    sInstance = new CollBookHelper();
                    daoSession  = DaoDbHelper.getInstance().getDaoSession();
                    collBookBeanDao = daoSession.getCollBookBeanDao();
                }
            }
        }

        return sInstance;
    }

    public void saveBook(CollBookBean collBookBean)
    {
        collBookBeanDao.insertOrReplace(collBookBean);
    }

    public void saveBooks(List<CollBookBean> collBookBeans)
    {
        collBookBeanDao.insertOrReplaceInTx(collBookBeans);
    }

    public Observable<String> removeBookInRx(CollBookBean collBookBean)
    {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                FileUtils.deleteFile(Constant.BOOK_CACHE_PATH + collBookBean.get_id());
                BookDownloadHelper.getInstance().removeDownloadTask(collBookBean.get_id());
                BookChapterHelper.getInstance().removeChapters(collBookBean.get_id());

                collBookBeanDao.delete(collBookBean);
                e.onNext("delete success!");
            }
        });
    }

    public CollBookBean findBookById(String id)
    {
        return collBookBeanDao.queryBuilder().where(CollBookBeanDao.Properties._id.eq(id)).unique();
    }

    public List<CollBookBean> findAllBooks()
    {
        return collBookBeanDao.queryBuilder().orderDesc(CollBookBeanDao.Properties.LastRead).list();
    }

    public void saveBookWithAsync(CollBookBean collBookBean) {
        daoSession.startAsyncSession().runInTx(() -> {
            if (collBookBean.getBookChapters() != null) {
                //存储BookChapterBean(需要找个免更新的方式)
                daoSession.getBookChapterBeanDao()
                        .insertOrReplaceInTx(collBookBean.getBookChapters());
            }
            //存储CollBook (确保先后顺序，否则出错)
            collBookBeanDao.insertOrReplace(collBookBean);
        });
    }

}
