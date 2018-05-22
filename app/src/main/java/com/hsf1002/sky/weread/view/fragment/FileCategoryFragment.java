package com.hsf1002.sky.weread.view.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.model.LocalFileBean;
import com.hsf1002.sky.weread.utils.FileStack;
import com.hsf1002.sky.weread.utils.FileUtils;
import com.hsf1002.sky.weread.view.adapter.LocalFileAdapter;
import com.hsf1002.sky.weread.view.base.BaseFileFragment;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;
import com.hsf1002.sky.weread.widget.DividerItemDecoration;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hefeng on 18-5-21.
 */

public class FileCategoryFragment extends BaseFileFragment {

    @BindView(R.id.file_category_tv_path)
    TextView pathTv;

    @BindView(R.id.file_category_tv_back_last)
    TextView backlastTv;

    @BindView(R.id.rv_file_category)
    RecyclerView recyclerView;

    private List<LocalFileBean> fileBeanList = new ArrayList<>();
    private FileStack fileStack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_file_category, new BaseViewModel(context));

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public static FileCategoryFragment newInstance()
    {
        FileCategoryFragment fragment = new FileCategoryFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();

        fileStack = new FileStack();

        mAdapter = new LocalFileAdapter(fileBeanList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(mAdapter);

        File root = Environment.getExternalStorageDirectory();
        toggleFileTree(root);

        mAdapter.setOnItemClickListener((adapter1, view, position) ->
        {
            File file = fileBeanList.get(position).getFile();

            if (file.isDirectory())
            {
                FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                snapshot.filePath = pathTv.getText().toString();
                snapshot.files = new ArrayList<File>(mAdapter.getAllFiles());
                snapshot.scrollOffset = recyclerView.computeVerticalScrollOffset();
                fileStack.push(snapshot);

                toggleFileTree(file);
            }
            else
            {
                String id = file.getAbsolutePath();

                if (CollBookHelper.getInstance().findBookById(id) != null)
                {
                    return;
                }

                mAdapter.setCheckedItem(position);

                if (mListener != null)
                {
                    mListener.onItemCheckedChange(mAdapter.getItemIsChecked(position));
                }
            }
        });

        backlastTv.setOnClickListener( v ->
        {
            FileStack.FileSnapshot snapshot = fileStack.pop();
            int oldScrollOffset = recyclerView.computeHorizontalScrollOffset();
            if (snapshot == null)
            {
                return;
            }

            pathTv.setText(snapshot.filePath);
            addFiles(snapshot.files);

            recyclerView.scrollBy(0, snapshot.scrollOffset - oldScrollOffset);

            if (mListener != null)
            {
                mListener.onCategoryChanged();
            }
        });
    }

    private void addFiles(List<File> files)
    {
        fileBeanList.clear();

        for (File file : files)
        {
            LocalFileBean localFileBean = new LocalFileBean();
            localFileBean.setSelect(false);
            localFileBean.setFile(file);
            fileBeanList.add(localFileBean);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void toggleFileTree(File file)
    {
        pathTv.setText(getString(R.string.path, file.getPath()));

        File[] files = file.listFiles(new SimpleFileFilter());
        List<File> rootfiles = Arrays.asList(files);

        Collections.sort(rootfiles, new FileComparator());
        addFilters(rootfiles);

        if (mListener != null)
        {
            mListener.onCategoryChanged();
        }
    }

    private void addFilters(List<File> files)
    {
        fileBeanList.clear();

        for (File file : files)
        {
            LocalFileBean localFileBean = new LocalFileBean();
            localFileBean.setSelect(false);
            localFileBean.setFile(file);
            fileBeanList.add(localFileBean);
        }

        mAdapter.notifyDataSetChanged();
    }

    public class FileComparator implements Comparator<File>
    {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile())
            {
                return -1;
            }

            if (o2.isDirectory() && o1.isFile())
            {
                return 1;
            }

            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public class SimpleFileFilter implements FileFilter
    {
        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(".")) {
                return false;
            }

            if (pathname.isDirectory() && pathname.list().length == 0)
            {
                return false;
            }

            if (!pathname.isDirectory() && (pathname.length() == 0 || !pathname.getName().endsWith(FileUtils.SUFFIX_TXT)))
            {
                return false;
            }

            return true;
        }
    }
}
