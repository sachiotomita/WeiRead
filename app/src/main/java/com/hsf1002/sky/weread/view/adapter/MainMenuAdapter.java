package com.hsf1002.sky.weread.view.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.model.MainMenuBean;
import com.hsf1002.sky.weread.utils.DimenUtils;

import java.util.List;

/**
 * Created by hefeng on 18-5-10.
 */

public class MainMenuAdapter extends BaseQuickAdapter<MainMenuBean, BaseViewHolder> {

    public MainMenuAdapter(@Nullable List<MainMenuBean> data) {
        super(R.layout.adapter_main_menu);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainMenuBean item) {
        TextView name = helper.getView(R.id.tv_name);
        name.setText(item.getName());
        name.setCompoundDrawablePadding(DimenUtils.dp2px(10));
        name.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(item.getIcon()), null, null, null);
    }
}
