package com.hsf1002.sky.weread.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.model.BookClassifyBean;

import java.util.List;

/**
 * Created by hefeng on 18-5-14.
 */

public class ClassifyAdapter extends BaseQuickAdapter<BookClassifyBean.ClassifyBean, BaseViewHolder> {

    public ClassifyAdapter( @Nullable List<BookClassifyBean.ClassifyBean> data) {
        super(R.layout.item_classify, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookClassifyBean.ClassifyBean item) {
        helper.setText(R.id.tv_name, item.getName()).setText(R.id.tv_count, item.getBookCount() + " s");

        Glide.with(mContext).load(Constant.BASE_URL + item.getIcon())
                .apply(new RequestOptions().placeholder(R.drawable.ic_default))
                .into((ImageView)helper.getView(R.id.iv_icon));
    }
}
