package com.hsf1002.sky.weread.view.fragment;

import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.model.BookClassifyBean;
import com.hsf1002.sky.weread.view.base.IBaseDataView;

/**
 * Created by hefeng on 18-5-14.
 */

public interface IClassifyBook extends IBaseDataView{
    void getBookClassify(BookClassifyBean bookClassifyBean);
}
