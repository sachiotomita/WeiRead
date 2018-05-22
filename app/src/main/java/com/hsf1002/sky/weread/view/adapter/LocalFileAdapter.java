package com.hsf1002.sky.weread.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.model.LocalFileBean;
import com.hsf1002.sky.weread.utils.FileUtils;
import com.hsf1002.sky.weread.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hefeng on 18-5-21.
 */

public class LocalFileAdapter extends BaseQuickAdapter<LocalFileBean, BaseViewHolder> {
    private int checkedCount = 0;

    public LocalFileAdapter(@Nullable List<LocalFileBean> data) {
        super(R.layout.item_local_file, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalFileBean item) {
        File file  = item.getFile();

        if (file.isDirectory())
        {
            helper.setVisible(R.id.file_iv_icon, true)
                    .setImageResource(R.id.file_iv_icon, R.drawable.ic_dir)
                    .setVisible(R.id.file_cb_select, false)
                    .setVisible(R.id.file_ll_brief, false)
                    .setText(R.id.file_tv_name, file.getName())
                    .setVisible(R.id.file_tv_sub_count, true)
                    .setText(R.id.file_tv_sub_count, mContext.getString(R.string.sub_count, file.list().length));
        }
        else
        {
            if (CollBookHelper.getInstance().findBookById(item.getFile().getAbsolutePath()) != null)
            {
                helper.setVisible(R.id.file_cb_select, false)
                        .setVisible(R.id.file_iv_icon, true)
                        .setImageResource(R.id.file_iv_icon, R.drawable.ic_coll_book);
            } else
            {
                helper.setVisible(R.id.file_cb_select, true)
                        .setChecked(R.id.file_cb_select, item.isSelect())
                        .setVisible(R.id.file_iv_icon, false);
            }

            helper.setVisible(R.id.file_ll_brief, true)
                    .setVisible(R.id.file_tv_sub_count, false)
                    .setText(R.id.file_tv_name, file.getName())
                    .setText(R.id.file_tv_size, FileUtils.getFileSize(file.length()))
                    .setText(R.id.file_tv_date, StringUtils.dateConvert(file.lastModified(), Constant.FORMAT_FILE_DATE));

        }
    }

    public void setCheckedAll(boolean isChecked) {
        checkedCount = 0;

        for (LocalFileBean localFileBean : mData) {
            if (localFileBean.getFile().isFile() && !isFileLoaded(localFileBean.getFile().getAbsolutePath())) {
                localFileBean.setSelect(isChecked);
                if (isChecked) {
                    ++checkedCount;
                }
            }
        }

        notifyDataSetChanged();
    }

    public int getCheckedCount() {
        return checkedCount;
    }

    public List<LocalFileBean> getCheckedFiles() {
        List<LocalFileBean> beans = new ArrayList<>();
        for (LocalFileBean localFileBean : mData) {
            if (localFileBean.isSelect()) {
                beans.add(localFileBean);
            }
        }
        return beans;
    }

    public void removeCheckedItems(List<LocalFileBean> localFileBeans) {
        mData.removeAll(localFileBeans);
        checkedCount -= localFileBeans.size();
        notifyDataSetChanged();
    }

    private boolean isFileLoaded(String id) {
        //如果是已加载的文件，则点击事件无效。
        if (CollBookHelper.getInstance().findBookById(id) != null) {
            return true;
        }
        return false;
    }

    public void setCheckedItem(int position) {
        LocalFileBean bean = mData.get(position);

        //如果是已加载的文件，则点击事件无效。
        if (isFileLoaded(bean.getFile().getAbsolutePath()))
        {
            return;
        }

        if (bean.isSelect()) {
            bean.setSelect(false);
            --checkedCount;
        } else {
            bean.setSelect(true);
            ++checkedCount;
        }
        notifyDataSetChanged();
    }

    public boolean getItemIsChecked(int pos) {
        return mData.get(pos).isSelect();
    }

    public List<File> getAllFiles() {
        List<File> files = new ArrayList<>();

        for (LocalFileBean localFileBean : mData) {
            files.add(localFileBean.getFile());
        }
        return files;
    }
}
