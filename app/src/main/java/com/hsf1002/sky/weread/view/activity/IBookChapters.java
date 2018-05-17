package com.hsf1002.sky.weread.view.activity;

import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

/**
 * Created by hefeng on 18-5-16.
 */

public interface IBookChapters  extends IBaseLoadView{
    void bookChapters(BookChapterBean bookChapterBean);
    void finishChapters();
    void errorChapters();
}
