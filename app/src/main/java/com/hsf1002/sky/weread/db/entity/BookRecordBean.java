package com.hsf1002.sky.weread.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hefeng on 18-5-11.
 */

@Entity
public class BookRecordBean {
    private String bookId;
    private int chapter;
    private int pagePos;
    @Generated(hash = 340380968)
    public BookRecordBean(String bookId, int chapter, int pagePos) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.pagePos = pagePos;
    }
    @Generated(hash = 398068002)
    public BookRecordBean() {
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getChapter() {
        return this.chapter;
    }
    public void setChapter(int chapter) {
        this.chapter = chapter;
    }
    public int getPagePos() {
        return this.pagePos;
    }
    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

}
