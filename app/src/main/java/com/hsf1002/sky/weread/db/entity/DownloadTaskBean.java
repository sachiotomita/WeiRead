package com.hsf1002.sky.weread.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;


import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.hsf1002.sky.weread.db.gen.DaoSession;
import com.hsf1002.sky.weread.db.gen.BookChapterBeanDao;
import com.hsf1002.sky.weread.db.gen.DownloadTaskBeanDao;

/**
 * Created by hefeng on 18-5-11.
 */

@Entity
public class DownloadTaskBean {
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_WAIT = 2;
    public static final int STATUS_PAUSE = 3;
    public static final int STATUS_ERROR = 4;
    public static final int STATUS_FINISH = 5;

    @Id
    private String taskName;
    private String bookId;

    @ToMany(referencedJoinProperty = "taskName")
    private List<BookChapterBean> bookChapterList;

    private int currentChapter = 0;
    private int lastChapter = 0;

    private volatile int status = STATUS_WAIT;
    private long size = 0;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1584592296)
    private transient DownloadTaskBeanDao myDao;
    @Generated(hash = 597395122)
    public DownloadTaskBean(String taskName, String bookId, int currentChapter,
            int lastChapter, int status, long size) {
        this.taskName = taskName;
        this.bookId = bookId;
        this.currentChapter = currentChapter;
        this.lastChapter = lastChapter;
        this.status = status;
        this.size = size;
    }
    @Generated(hash = 2123101309)
    public DownloadTaskBean() {
    }
    public String getTaskName() {
        return this.taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getCurrentChapter() {
        return this.currentChapter;
    }
    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }
    public int getLastChapter() {
        return this.lastChapter;
    }
    public void setLastChapter(int lastChapter) {
        this.lastChapter = lastChapter;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 389263273)
    public List<BookChapterBean> getBookChapterList() {
        if (bookChapterList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookChapterBeanDao targetDao = daoSession.getBookChapterBeanDao();
            List<BookChapterBean> bookChapterListNew = targetDao
                    ._queryDownloadTaskBean_BookChapterList(taskName);
            synchronized (this) {
                if (bookChapterList == null) {
                    bookChapterList = bookChapterListNew;
                }
            }
        }
        return bookChapterList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1077762221)
    public synchronized void resetBookChapterList() {
        bookChapterList = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1923117869)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDownloadTaskBeanDao() : null;
    }

}
