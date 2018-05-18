package com.hsf1002.sky.weread.view.activity;

import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

/**
 * Created by hefeng on 18-5-17.
 */

public interface IBookDetail extends IBaseLoadView{
    void getBookInfo(BookBean bookBean);
}
