package com.hsf1002.sky.weread.view.base;

import com.hsf1002.sky.weread.model.LocalFileBean;
import com.hsf1002.sky.weread.view.adapter.LocalFileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hefeng on 18-5-21.
 */

public abstract class BaseFileFragment extends BaseFragment {
    protected LocalFileAdapter mAdapter;
    protected OnFileCheckedListener mListener;
    protected boolean isCheckedAll;

    public void setCheckedAll(boolean checkedAll) {
        if (mAdapter == null)
        {
            return;
        }

        isCheckedAll = checkedAll;
        mAdapter.setCheckedAll(checkedAll);
    }

    //当前fragment是否全选
    public boolean isCheckedAll() {
        return isCheckedAll;
    }

    //获取被选中的数量
    public int getCheckedCount() {
        if (mAdapter == null)
        {
            return 0;
        }
        return mAdapter.getCheckedCount();
    }

    public void setChecked(boolean checked) {
        isCheckedAll = checked;
    }

    public List<File> getCheckedFiles() {
        List<File> files = new ArrayList<>();
        if (mAdapter != null) {
            for (LocalFileBean localFileBean : mAdapter.getCheckedFiles()) {
                files.add(localFileBean.getFile());
            }
        }
        return files;
    }

    public void deleteCheckedFiles() {
        //删除选中的文件
        List<LocalFileBean> files = mAdapter.getCheckedFiles();
        //删除显示的文件列表
        mAdapter.removeCheckedItems(files);
        //删除选中的文件
        for (LocalFileBean localFileBean : files) {
            if (localFileBean.getFile().exists()) {
                localFileBean.getFile().delete();
            }
        }
    }

    //设置文件点击监听事件
    public void setOnFileCheckedListener(OnFileCheckedListener listener) {
        mListener = listener;
    }

    //文件点击监听
    public interface OnFileCheckedListener {
        void onItemCheckedChange(boolean isChecked);
        void onCategoryChanged();
    }
}
