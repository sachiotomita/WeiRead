package com.hsf1002.sky.weread.view.fragment;

import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.view.base.BaseFragment;

import java.util.List;

/**
 * Created by hefeng on 18-5-11.
 */

public class BookShelfFragment extends BaseFragment implements IBookShelf {

    public static BookShelfFragment newInstance()
    {
        BookShelfFragment fragment = new BookShelfFragment();
        return fragment;
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void bookShelfInfo(List<CollBookBean> beans) {

    }

    @Override
    public void bookInfo(CollBookBean bean) {

    }

    @Override
    public void deleteSuccess() {

    }
}
