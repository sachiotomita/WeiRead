package com.hsf1002.sky.weread.view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.db.entity.DownloadTaskBean;
import com.hsf1002.sky.weread.db.helper.BookRecordHelper;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.event.DeleteResponseEvent;
import com.hsf1002.sky.weread.event.DeleteTaskEvent;
import com.hsf1002.sky.weread.event.DownloadMessage;
import com.hsf1002.sky.weread.utils.LoadingHelper;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxBus;
import com.hsf1002.sky.weread.utils.rxhelper.RxUtils;
import com.hsf1002.sky.weread.view.activity.ReadActivity;
import com.hsf1002.sky.weread.view.adapter.BookShelfAdapter;
import com.hsf1002.sky.weread.view.base.BaseFragment;
import com.hsf1002.sky.weread.viewmodel.fragment.VMBookShelf;
import com.hsf1002.sky.weread.viewmodel.fragment.VMBooksInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by hefeng on 18-5-11.
 */

public class BookShelfFragment extends BaseFragment implements IBookShelf {

    @BindView(R.id.rv_book_shelf)
    RecyclerView recyclerView;

    //@BindView(R.id.refresh)
    //SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private BookShelfAdapter adapter;
    private List<CollBookBean> bookBeans = new ArrayList<>();
    private VMBookShelf model;
    private boolean isCheck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = new VMBookShelf(context, this);
        View view = setContentView(container, R.layout.fragment_book_shelf, model);

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    public static BookShelfFragment newInstance()
    {
        BookShelfFragment fragment = new BookShelfFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();

        adapter = new BookShelfAdapter(bookBeans);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        /*smartRefreshLayout.setOnRefreshListener(refreshlayout ->
        {
            bookBeans.clear();
            bookBeans.addAll(CollBookHelper.getInstance().findAllBooks());
            adapter.notifyDataSetChanged();
            model.getBookShelf(CollBookHelper.getInstance().findAllBooks());
        });*/

        swipeRefreshLayout.setOnRefreshListener( ()->
                {
                        bookBeans.clear();
                        bookBeans.addAll(CollBookHelper.getInstance().findAllBooks());
                        adapter.notifyDataSetChanged();
                        model.getBookShelf(CollBookHelper.getInstance().findAllBooks());
                }
        );

        adapter.setOnItemClickListener((adapter1, view, position) ->
        {
            CollBookBean collBookBean = adapter.getItem(position);

            if (collBookBean.getIsLocal())
            {
                String path = collBookBean.get_id();
                File file = new File(path);
                if (file.exists())
                {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, collBookBean);
                    bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, true);
                    startActivity(ReadActivity.class, bundle);
                }
                else
                {
                    new MaterialDialog.Builder(context)
                            .title(getResources().getString(R.string.tip))
                            .content("file not existed, delete?")
                            .positiveText(getResources().getString(R.string.sure))
                            .onPositive((dialog, which) -> deleteBook(collBookBean, position))
                            .negativeText(getResources().getString(R.string.cancel))
                            .onNegative((dialog, which) -> dialog.dismiss()).show();

                }
            }
            else
            {
                model.setBookInfo(collBookBean);
            }
        });

        // add download task
        Disposable downloadDisp = RxBus.getInstance()
                .toObservable(DownloadMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event ->
                {
                    ToastUtils.show(event.message);
                });
        addDisposable(downloadDisp);

        // delete book task
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteResponseEvent ->
                {
                    if (deleteResponseEvent.isDelete)
                    {
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("deleting...");
                        progressDialog.show();

                        CollBookHelper.getInstance().removeBookInRx(deleteResponseEvent.collBook)
                                .compose(RxUtils::toSimpleSingle)
                                .subscribe(Void ->
                                {
                                    progressDialog.dismiss();
                                    bookBeans.clear();
                                    bookBeans.addAll(CollBookHelper.getInstance().findAllBooks());
                                    adapter.notifyDataSetChanged();
                                });
                    }
                    else
                    {
                        AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                .setTitle("downloading...")
                                .setMessage("please first pause then delete")
                                .setPositiveButton("sure", ((dialog, which) ->
                                {
                                    dialog.dismiss();
                                })).create();
                        tipDialog.show();

                    }
                });
        addDisposable(deleteDisp);

        adapter.setOnItemClickListener((adapter1, view, position) ->
        {
            openItemDialog(bookBeans.get(position), position);
        });
    }

    private void openItemDialog(CollBookBean collBookBean, int position)
    {
        String[] menus = getResources().getStringArray(R.array.menu_local_book);

        new MaterialDialog.Builder(context)
                .title(collBookBean.getTitle())
                .items(menus)
                .itemsCallback((dialog, itemView, position1, text) -> onItemMenuClick(menus[position1], collBookBean, position))
                .show();
    }

    private void onItemMenuClick(String which, CollBookBean collBook, int position)
    {
        switch (which)
        {
            case "cache":
                deleteBook(collBook, position);
                break;
            case "delete":
                downloadBook(collBook);
                break;
            default:
                break;
        }
    }

    private void downloadBook(CollBookBean collBookBean)
    {
        DownloadTaskBean task = new DownloadTaskBean();
        task.setTaskName(collBookBean.getTitle());
        task.setBookId(collBookBean.get_id());
        task.setBookChapters(collBookBean.getBookChapters());
        task.setLastChapter(collBookBean.getBookChapters().size());

        RxBus.getInstance().post(task);
    }

    private void deleteBook(CollBookBean collBook, int position)
    {
        if (collBook.getIsLocal()) {
            new MaterialDialog.Builder(context)
                    .title("delete local book")
                    .checkBoxPrompt("delete local file at the same time", false, (buttonView, isChecked) -> isCheck = isChecked)
                    .positiveText(R.string.sure)
                    .onPositive((dialog, which) ->
                    {
                        if (isCheck)
                        {
                            LoadingHelper.getInstance().showLoading(context);

                            File file = new File(collBook.get_id());
                            if (file.exists())
                            {
                                file.delete();
                            }

                            CollBookHelper.getInstance().removeBookInRx(collBook)
                                    .subscribe(s ->
                                    {
                                        ToastUtils.show(s);
                                        BookRecordHelper.getInstance().removeBook(collBook.get_id());
                                        adapter.remove(position);
                                        LoadingHelper.getInstance().hideLoading();
                                    }
                                    , throwable ->
                                            {
                                                ToastUtils.show("delete failed!");
                                                LoadingHelper.getInstance().hideLoading();
                                            });
                        }
                        else
                        {
                            CollBookHelper.getInstance().removeBookInRx(collBook);
                            BookRecordHelper.getInstance().removeBook(collBook.get_id());
                            adapter.remove(position);
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .negativeText(R.string.cancel)
                    .onNegative((dialog, which) ->
                    {
                        dialog.dismiss();
                    })
                    .show();

        }
        else
        {
            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
            model.deleteBookShelfFromServer(collBook);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        bookBeans.clear();
        bookBeans.addAll(CollBookHelper.getInstance().findAllBooks());
        adapter.notifyDataSetChanged();
        model.getBookShelf(CollBookHelper.getInstance().findAllBooks());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {
        //smartRefreshLayout.finishRefresh();
    }

    @Override
    public void bookShelfInfo(List<CollBookBean> beans) {
        bookBeans.addAll(beans);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void bookInfo(CollBookBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, bean);
        bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, true);
        startActivity(ReadActivity.class, bundle);
    }

    @Override
    public void deleteSuccess() {
        adapter.notifyDataSetChanged();
    }
}
