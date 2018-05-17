package com.hsf1002.sky.weread.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.db.entity.CollBookBean;

import java.util.List;

/**
 * Created by hefeng on 18-5-16.
 */

public class BookShelfAdapter extends BaseQuickAdapter<CollBookBean, BaseViewHolder> {
    public BookShelfAdapter(@Nullable List<CollBookBean> data) {
        super(R.layout.item_book_shelf, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollBookBean item) {
        if (item.getIsLocal())
        {
            helper.setImageResource(R.id.coll_book_iv_cover, R.drawable.ic_base_local_book)
                    .setVisible(R.id.coll_book_tv_lately_update, true);
        }
        else
        {
            Glide.with(mContext).load(Constant.ZHUISHU_IMAGE_URL + item.getCover())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_book_loading))
                    .into((ImageView)helper.getView(R.id.coll_book_iv_cover));
        }

        helper.setText(R.id.coll_book_tv_name, item.getTitle())
                .setText(R.id.coll_book_tv_chapter, item.getLastChapter());

        if (item.getIsUpdate())
        {
            helper.setVisible(R.id.coll_book_iv_red_rot, true);
        }
        else
        {
            helper.setVisible(R.id.coll_book_iv_red_rot, false);
        }
    }
}
