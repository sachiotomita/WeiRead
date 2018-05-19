package com.hsf1002.sky.weread.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.widget.page.TxtChapter;

import java.util.List;

/**
 * Created by hefeng on 18-5-18.
 */

public class ReadCategoryAdapter extends BaseQuickAdapter<TxtChapter, BaseViewHolder> {
    public ReadCategoryAdapter(@Nullable List<TxtChapter> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TxtChapter item) {
        Drawable drawable = null;

        if (item.getLink() == null)
        {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_load);
        }
        else
        {
            if (item.getBookId() != null)
            {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_load);
            }
            else
            {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.selector_category_unload);
            }
        }

        TextView category = helper.getView(R.id.category_tv_chapter);
        category.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        category.setSelected(item.isSelect());
        category.setText(item.getTitle());

        if (item.isSelect())
        {
            category.setTextColor(ContextCompat.getColor(mContext, R.color.color_ec4a48));
        }
        else
        {
            category.setTextColor(ContextCompat.getColor(mContext, R.color.black));
        }
    }
}
