package com.hsf1002.sky.weread.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.model.ReadBgBean;

import java.util.List;

/**
 * Created by hefeng on 18-5-18.
 */

public class ReadBgAdapter extends BaseQuickAdapter<ReadBgBean, BaseViewHolder> {

    public ReadBgAdapter(@Nullable List<ReadBgBean> data) {
        super(R.layout.item_read_bg, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReadBgBean item) {
        helper.setBackgroundRes(R.id.read_bg_view, item.getBgColor());
        helper.setVisible(R.id.read_bg_iv_checked, item.isSelect());
    }
}
