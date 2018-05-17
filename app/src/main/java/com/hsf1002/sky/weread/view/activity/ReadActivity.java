package com.hsf1002.sky.weread.view.activity;

import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.view.base.BaseActivity;

/**
 * Created by hefeng on 18-5-16.
 */

public class ReadActivity extends BaseActivity implements IBookChapters{

    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void bookChapters(BookChapterBean bookChapterBean) {

    }

    @Override
    public void finishChapters() {

    }

    @Override
    public void errorChapters() {

    }
}
