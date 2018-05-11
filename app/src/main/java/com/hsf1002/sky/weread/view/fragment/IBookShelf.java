package com.hsf1002.sky.weread.view.fragment;

import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.view.base.IBaseLoadView;

import java.util.List;

/**
 * Created by hefeng on 18-5-11.
 */

public interface IBookShelf extends IBaseLoadView {
    void bookShelfInfo(List<CollBookBean> beans);
    void bookInfo(CollBookBean bean);
    void deleteSuccess();
}
