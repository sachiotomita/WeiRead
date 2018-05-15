package com.hsf1002.sky.weread.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.utils.BaseUtils;

import java.util.List;

/**
 * Created by hefeng on 18-5-14.
 */

public class BookInfoAdapter extends BaseQuickAdapter<BookBean, BaseViewHolder> {

    public BookInfoAdapter(@Nullable List<BookBean> data) {
        super(R.layout.item_bookinfo, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookBean item) {
        String wordCount = item.getLatelyFollower() / 10000 > 0 ? BaseUtils.format1Digits((double)item.getLatelyFollower() / 10000 ) + "万" : item.getLatelyFollower() + "";

        helper.setText(R.id.book_brief_tv_title, item.getTitle())
                .setText(R.id.book_brief_tv_author, item.getAuthor() + "  |  " + item.getMajorCate())
                .setText(R.id.book_brief_tv_brief, item.getLongIntro())
                .setText(R.id.ctv_arrow_count, wordCount + " ")
                .setText(R.id.ctv_retention, item.getRetentionRatio() + "%");

        Glide.with(mContext).load(Constant.ZHUISHU_IMAGE_URL + item.getCover())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_book_loading))
                .into((ImageView)helper.getView(R.id.book_brief_iv_portrait));
    }
}
