package com.hsf1002.sky.weread.fragment;

import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

import java.util.List;

/**
 * Created by hefeng on 18-5-10.
 */

public interface IBookSearchInfo extends IBaseLoadView{
    void getSearchBooks(List<BookBean> bookBeans);
}
