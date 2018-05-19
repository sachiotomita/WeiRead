package com.hsf1002.sky.weread.view.activity;

import com.hsf1002.sky.weread.model.BookChaptersBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

/**
 * Created by hefeng on 18-5-16.
 */

public interface IBookChapters  extends IBaseLoadView{
    void bookChapters(BookChaptersBean bookChaptersBean);
    void finishChapters();
    void errorChapters();
}
