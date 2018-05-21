package com.hsf1002.sky.weread.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.utils.StringUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.activity.MainActivity;
import com.hsf1002.sky.weread.view.adapter.BaseViewPageAdapter;
import com.hsf1002.sky.weread.view.base.BaseFileFragment;
import com.hsf1002.sky.weread.view.base.BaseFragment;
import com.hsf1002.sky.weread.viewmodel.BaseViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hefeng on 18-5-11.
 */

public class ScanBookFragment extends BaseFragment {
    @BindView(R.id.nts_scan)
    NavigationTabStrip navigationTabStrip;

    @BindView(R.id.vp_scan)
    ViewPager viewPager;

    @BindView(R.id.file_system_cb_selected_all)
    CheckBox selectedCb;

    @BindView(R.id.file_system_btn_add_book)
    Button addbookBtn;

    @BindView(R.id.file_system_btn_delete)
    Button deleteBtn;

    private String[] titles = {};
    private List<Fragment> fragments;
    private BaseFileFragment currentFragment;
    private LocalBookFragment localBookFragment;
    private FileCategoryFragment categoryFragment;

    private BaseFileFragment.OnFileCheckedListener listener = new BaseFileFragment.OnFileCheckedListener() {
        @Override
        public void onItemCheckedChange(boolean isChecked) {
            changeMenuStatus();
        }

        @Override
        public void onCategoryChanged() {
            currentFragment.setCheckedAll(false);
            changeMenuStatus();
            changeCheckedAllStatus();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = setContentView(container, R.layout.fragment_scan_book, new BaseViewModel(context));

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public static ScanBookFragment newInstance()
    {
        ScanBookFragment fragment = new ScanBookFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();

        fragments = new ArrayList<>();
        localBookFragment = LocalBookFragment.newInstance();
        categoryFragment = FileCategoryFragment.newInstance();
        currentFragment = localBookFragment;

        fragments.add(localBookFragment);
        fragments.add(categoryFragment);

        viewPager.setAdapter(new BaseViewPageAdapter(getActivity().getSupportFragmentManager(), titles, fragments));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((MainActivity)getActivity()).setLeftSlide(position == 0);
                if (position == 0)
                {
                    currentFragment = localBookFragment;
                }
                else
                {
                    currentFragment = categoryFragment;
                }

                changeMenuStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigationTabStrip.setTitles(titles);
        navigationTabStrip.setViewPager(viewPager);

        localBookFragment.setOnFileCheckedListener(listener);
    }

    @OnClick({R.id.file_system_cb_selected_all, R.id.file_system_btn_add_book, R.id.file_system_btn_delete})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.file_system_cb_selected_all:
                currentFragment.setCheckedAll(selectedCb.isChecked());
                changeMenuStatus();
                break;
            case R.id.file_system_btn_add_book:
                List<File> files = currentFragment.getCheckedFiles();
                List<CollBookBean> collbooks = convertCollBook(files);
                CollBookHelper.getInstance().saveBooks(collbooks);
                currentFragment.setCheckedAll(false);
                changeMenuStatus();
                changeCheckedAllStatus();
                ToastUtils.show(getResources().getString(R.string.add_succeed, collbooks.size()));
                break;
            case R.id.file_system_btn_delete:
                new AlertDialog.Builder(context)
                        .setTitle(getString(R.string.delete_file))
                        .setMessage(getString(R.string.delete))
                        .setPositiveButton(getResources().getString(R.string.sure), (dialog, which) ->
                        {
                            currentFragment.deleteCheckedFiles();
                            ToastUtils.show(getString(R.string.delete_success));
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), null)
                        .show();
                break;
            default:
                break;
        }
    }

    private List<CollBookBean> convertCollBook(List<File> files) {
        List<CollBookBean> collBooks = new ArrayList<>(files.size());
        for (File file : files) {
            //判断文件是否存在
            if (!file.exists())
            {
                continue;
            }

            CollBookBean collBook = new CollBookBean();
            collBook.setIsLocal(true);
            collBook.set_id(file.getAbsolutePath());
            collBook.setTitle(file.getName().replace(".txt", ""));
            collBook.setLastChapter(getString(R.string.start_read));
            collBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            collBooks.add(collBook);
        }
        return collBooks;
    }

    private void changeMenuStatus()
    {
        if (currentFragment.getCheckedCount() == 0)
        {
            addbookBtn.setText(getString(R.string.add_shelf));

            setMenuClickable(false);

            if (selectedCb.isChecked()) {
                currentFragment.setChecked(false);
                selectedCb.setChecked(currentFragment.isCheckedAll());
            }
        }
        else
        {
            addbookBtn.setText(getString(R.string.add_shelves, currentFragment.getCheckedCount()));
            setMenuClickable(true);

            if (currentFragment.getCheckedCount() == currentFragment.getCheckedCount())
            {
                currentFragment.setChecked(true);
                selectedCb.setChecked(currentFragment.isCheckedAll());
            }
            else if (currentFragment.isCheckedAll())
            {
                currentFragment.setChecked(false);
                selectedCb.setChecked(currentFragment.isCheckedAll());
            }
        }

        if (currentFragment.isCheckedAll())
        {
            selectedCb.setText(getString(R.string.cancel));
        }
        else
        {
            selectedCb.setText(getString(R.string.select_all));
        }
    }

    private void changeCheckedAllStatus()
    {
        int count = currentFragment.getCheckedCount();

        if (count > 0) {
            selectedCb.setClickable(true);
            selectedCb.setEnabled(true);
        } else {
            selectedCb.setClickable(false);
            selectedCb.setEnabled(false);
        }
    }

    private void setMenuClickable(boolean isClickable)
    {
        deleteBtn.setEnabled(isClickable);
        deleteBtn.setClickable(isClickable);

        addbookBtn.setEnabled(isClickable);
        addbookBtn.setClickable(isClickable);
    }
}
