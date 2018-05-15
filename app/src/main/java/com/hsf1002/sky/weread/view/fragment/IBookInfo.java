package com.hsf1002.sky.weread.view.fragment;

import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.view.base.IBaseDataView;

import java.util.List;

/**
 * Created by hefeng on 18-5-14.
 */

public interface IBookInfo extends IBaseDataView {
    void getBooks(List<BookBean> bookBeans, boolean isLoadMore);
}
