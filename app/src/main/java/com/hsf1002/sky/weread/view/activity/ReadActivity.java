package com.hsf1002.sky.weread.view.activity;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.db.entity.BookChapterBean;
import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.db.helper.BookChapterHelper;
import com.hsf1002.sky.weread.model.BookChaptersBean;
import com.hsf1002.sky.weread.utils.BrightnessUtils;
import com.hsf1002.sky.weread.utils.ReadSettingManager;
import com.hsf1002.sky.weread.utils.ScreenUtils;
import com.hsf1002.sky.weread.utils.StatusBarUtils;
import com.hsf1002.sky.weread.utils.StringUtils;
import com.hsf1002.sky.weread.utils.rxhelper.RxUtils;
import com.hsf1002.sky.weread.view.adapter.ReadCategoryAdapter;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMBookContentInfo;
import com.hsf1002.sky.weread.widget.dialog.ReadSettingDialog;
import com.hsf1002.sky.weread.widget.page.NetPageLoader;
import com.hsf1002.sky.weread.widget.page.PageLoader;
import com.hsf1002.sky.weread.widget.page.PageView;
import com.hsf1002.sky.weread.widget.page.TxtChapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static com.hsf1002.sky.weread.constant.Constant.BATTERY_LEVEL;
import static com.hsf1002.sky.weread.constant.Constant.KEEP_BRIGHT;
import static com.hsf1002.sky.weread.constant.Constant.SCREEN_AUTO_BRIGHTNESS_ADJ;

/**
 * Created by hefeng on 18-5-16.
 */

public class ReadActivity extends BaseActivity implements IBookChapters{
    @BindView(R.id.tv_toolbar_title)
    TextView title;

    @BindView(R.id.read_abl_top_menu)
    AppBarLayout appBarLayout;

    @BindView(R.id.pv_read_page)
    PageView readPage;

    @BindView(R.id.read_tv_page_tip)
    TextView readTipTv;

    @BindView(R.id.read_tv_pre_chapter)
    TextView readPreChapterTv;

    @BindView(R.id.read_sb_chapter_progress)
    SeekBar readSeekBar;

    @BindView(R.id.read_tv_next_chapter)
    TextView readNextTv;

    @BindView(R.id.read_tv_category)
    TextView readCategoryTv;

    @BindView(R.id.read_tv_night_mode)
    TextView readNightModeTv;

    @BindView(R.id.read_tv_setting)
    TextView readSettingTv;

    @BindView(R.id.read_ll_bottom_menu)
    LinearLayout readBottom;

    @BindView(R.id.rv_read_category)
    RecyclerView recyclerView;

    @BindView(R.id.read_dl_slide)
    DrawerLayout readDrawerLayout;

    private final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor(SCREEN_AUTO_BRIGHTNESS_ADJ);

    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

    private boolean isRegistered = false;

    private ReadSettingDialog settingDialog;
    private PageLoader pageLoader;
    private Animation topInAnim;
    private Animation topOutAnim;
    private Animation bottomInAnim;
    private Animation bottomOutAnim;

    private CollBookBean collBookBean;
    private PowerManager.WakeLock wakeLock;

    private boolean isCollected = false;
    private boolean isNightNode = false;
    private boolean isFullScreen = false;
    private String bookid;

    private ReadCategoryAdapter readCategoryAdapter;
    private List<TxtChapter> txtChapters = new ArrayList<>();
    private VMBookContentInfo model;
    List<BookChapterBean> bookChapterBeanList = new ArrayList<>();


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))
            {
                int level = intent.getIntExtra(BATTERY_LEVEL, 0);
                pageLoader.updateBattery(level);
            }
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK))
            {
                pageLoader.updateTime();
            }
        }
    };

    private ContentObserver brightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            // current brightness according to system
            if (selfChange || !settingDialog.isBrightFollowSystem())
            {
                return;
            }

            if (BRIGHTNESS_MODE_URI.equals(uri))
            {

            }
            else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(ReadActivity.this))
            {
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            }
            else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(ReadActivity.this))
            {
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            }
            else
            {

            }
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new VMBookContentInfo(context, this);
        setBiddingView(R.layout.activity_read, NO_BINDING, model);
    }

    @Override
    protected void initView() {
        super.initView();

        collBookBean = (CollBookBean)getIntent().getSerializableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED, false);
        isNightNode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();
        bookid = collBookBean.get_id();

        title.setText(collBookBean.getTitle());
        StatusBarUtils.transparencyBar(this);

        pageLoader = readPage.getPageLoader(collBookBean.getIsLocal());
        readDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        initData();

        settingDialog = new ReadSettingDialog(this, pageLoader);
        setCategory();

        toggleNightMode();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, intentFilter);

        if (ReadSettingManager.getInstance().isBrightnessAuto())
        {
            BrightnessUtils.setBrightness(this, BrightnessUtils.getScreenBrightness(this));
        }
        else
        {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, KEEP_BRIGHT);
        readPage.post(() -> hideSystemBar());

        initTopMenu();
        initBottomMenu();

        pageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onChapterChange(int pos) {
                setCategorySelect(pos);
            }

            @Override
            public void onLoadChapter(List<TxtChapter> chapters, int pos) {
                model.loadContent(bookid, chapters);
                setCategorySelect(pageLoader.getChapterPos());

                if (pageLoader.getPageStatus() == NetPageLoader.STATUS_LOADING || pageLoader.getPageStatus() == NetPageLoader.STATUS_EMPTY)
                {
                    readSeekBar.setEnabled(false);
                }

                readTipTv.setVisibility(View.GONE);
                readSeekBar.setProgress(0);
            }

            @Override
            public void onCategoryFinish(List<TxtChapter> chapters) {

            }

            @Override
            public void onPageCountChange(int count) {

            }

            @Override
            public void onPageChange(int pos) {

            }
        });
    }

    private void initData()
    {
        if (collBookBean.getIsLocal())
        {
            pageLoader.openBook(collBookBean);
        }
        else
        {
            if (isCollected)
            {
                Disposable disposable = BookChapterHelper.getInstance().findBookChaptersInRx(bookid)
                        .compose(RxUtils::toSimpleSingle)
                        .subscribe(bookChapterBeans ->
                                {
                                    collBookBean.setBookChapters(bookChapterBeans);
                                    pageLoader.openBook(collBookBean);

                                    if (collBookBean.getIsUpdate())
                                    {
                                        model.loadChapters(bookid);
                                    }
                                }
                        );
                model.addDisposable(disposable);
            }
            else
            {
                model.loadChapters(bookid);
            }
        }
    }

    private void setCategory()
    {
        readCategoryAdapter = new ReadCategoryAdapter(txtChapters);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(readCategoryAdapter);

        if (txtChapters.size() > 0)
        {
            setCategorySelect(0);
        }

        readCategoryAdapter.setOnItemClickListener((adapter, view, position) ->
        {
            setCategorySelect(0);
            readDrawerLayout.closeDrawer(Gravity.START);
            pageLoader.skipToChapter(position);
        });
    }

    private void setCategorySelect(int pos)
    {
        for (int i=0; i<txtChapters.size(); ++i)
        {
            TxtChapter chapter = txtChapters.get(i);

            if (i == pos)
            {
                chapter.setSelect(true);
            }
            else
            {
                chapter.setSelect(false);
            }
        }

        readCategoryAdapter.notifyDataSetChanged();
    }

    private void toggleNightMode()
    {
        if (isNightNode)
        {
            readNightModeTv.setText(StringUtils.getString(R.string.day));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_morning);
            readNightModeTv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
        else
        {
            readNightModeTv.setText(StringUtils.getString(R.string.night));
            Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.read_menu_night);
            readNightModeTv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void showSystemBar()
    {
        StatusBarUtils.showUnStableStatusBar(this);

        if (isFullScreen)
        {
            StatusBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar()
    {
        StatusBarUtils.hideUnStableStatusBar(this);

        if (isFullScreen)
        {
            StatusBarUtils.hideStableNavBar(this);
        }
    }

    private void initTopMenu()
    {
        if (Build.VERSION.SDK_INT >= 19 )
        {
            appBarLayout.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu()
    {
        if (ReadSettingManager.getInstance().isFullScreen())
        {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)appBarLayout.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            readBottom.setLayoutParams(params);
        }
        else
        {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)appBarLayout.getLayoutParams();
            params.bottomMargin = 0;
            readBottom.setLayoutParams(params);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void bookChapters(BookChaptersBean bookChaptersBean) {

    }

    @Override
    public void finishChapters() {

    }

    @Override
    public void errorChapters() {

    }
}
