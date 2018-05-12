package com.hsf1002.sky.weread.view.activity;

import android.Manifest;
import android.animation.Animator;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.db.entity.UserBean;
import com.hsf1002.sky.weread.db.helper.UserHelper;
import com.hsf1002.sky.weread.model.AppUpdateBean;
import com.hsf1002.sky.weread.model.MainMenuBean;
import com.hsf1002.sky.weread.utils.BaseUtils;
import com.hsf1002.sky.weread.utils.SharedPreUtils;
import com.hsf1002.sky.weread.utils.SnackBarUtils;
import com.hsf1002.sky.weread.utils.ThemeUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.adapter.MainMenuAdapter;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.view.fragment.BookClassifyFragment;
import com.hsf1002.sky.weread.view.fragment.BookShelfFragment;
import com.hsf1002.sky.weread.view.fragment.ScanBookFragment;
import com.hsf1002.sky.weread.widget.ResideLayout;
import com.hsf1002.sky.weread.widget.theme.ColorRelativeLayout;
import com.hsf1002.sky.weread.widget.theme.ColorUiUtil;
import com.hsf1002.sky.weread.widget.theme.Theme;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hsf1002.sky.weread.constant.Constant.USERNAME;


public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback, ISetting {

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @BindView(R.id.rv_menu)
    RecyclerView recyclerView;

    @BindView(R.id.tv_theme)
    TextView tvTheme;

    @BindView(R.id.tv_setting)
    TextView tvSettings;

    @BindView(R.id.top_menu)
    LinearLayout topMenu;

    @BindView(R.id.bottom_menu)
    LinearLayout bottomMenu;

    @BindView(R.id.menu)
    ColorRelativeLayout colorRelativeLayout;

    @BindView(R.id.frame_container)
    FrameLayout container;

    @BindView(R.id.resideLayout)
    ResideLayout resideLayout;

    @BindView(R.id.iv_toolbar_more)
    AppCompatImageView toolbarMore;

    @BindView(R.id.iv_toolbar_back)
    AppCompatImageView toolbarBack;

    @BindView(R.id.tv_toolbar_title)
    TextView title;

    private MainMenuAdapter mainMenuAdapter;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;
    private List<MainMenuBean>  menuBeans = new ArrayList<>();
    private long firstTime = 0;
    private String name;
    private VMSettingInfo model;

    private static final int CLASSIFY = 0;
    private static final int BOOKSHELF = 1;
    private static final int SCANBOOK = 2;
    //private static final int FEEDBACK = 3;
    //private static final int ABOUTAUTHOR = 4;
    private static int currentFragment = CLASSIFY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new VMSettingInfo(this, this);
        setBiddingView(R.layout.activity_main, NO_BINDING, model);
        initThemeToolBar(getToolBarTitleString(), R.drawable.ic_classify, R.drawable.ic_search, v ->
        {
            resideLayout.openPane();
        }, v ->
        {
            startActivity(BookSearchActivity.class);
        });

        model.appUpdate(false);
        fragmentManager = getSupportFragmentManager();
        initMenu();
        switchFragment(currentFragment);
    }

    private void initMenu()
    {
        tvDesc.setSelected(true);
        BaseUtils.setIconDrawable(tvSettings, R.drawable.ic_setting);
        BaseUtils.setIconDrawable(tvTheme, R.drawable.ic_theme);

        getMenuData();

        mainMenuAdapter = new MainMenuAdapter(menuBeans);
        recyclerView.setAdapter(mainMenuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        mainMenuAdapter.setOnItemClickListener(((adapter, view, position) ->
        {
            switch (position)
            {
                // classify
                case 0:
                {
                    currentFragment = BOOKSHELF;
                    switchFragment(currentFragment);
                    title.setText(menuBeans.get(position).getName());
                    toolbarBack.setImageResource(menuBeans.get(position).getIcon());
                    resideLayout.closePane();
                }
                break;
                // bookshelf
                case 1: {
                    currentFragment = BOOKSHELF;
                    switchFragment(currentFragment);
                    title.setText(menuBeans.get(position).getName());
                    toolbarBack.setImageResource(menuBeans.get(position).getIcon());
                    resideLayout.closePane();
                }
                break;
                // scan book
                case 2: {
                    currentFragment = SCANBOOK;
                    RxPermissions rxPermissions = new RxPermissions(this);
                    rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(permission -> {
                                if (permission.granted)
                                {
                                    MainActivity.this.switchFragment(position);
                                    title.setText(menuBeans.get(position).getName());
                                    toolbarBack.setImageResource(menuBeans.get(position).getIcon());
                                    resideLayout.closePane();
                                }
                                else if (permission.shouldShowRequestPermissionRationale)
                                {
                                    ToastUtils.show(R.string.customer_refused);
                                    resideLayout.closePane();
                                }
                                else
                                {
                                    resideLayout.closePane();
                                    SnackBarUtils.makeShort(MainActivity.this.getWindow().getDecorView(), getString(R.string.read_write_permission_defied)).show(getString(R.string.go_to_set), new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View v) {
                                            BaseUtils.getAppDetailSettingIntent(context, getPackageName());
                                        }
                                    });
                                }
                            });
                }
                break;
                // feedback
                case 3: {
                    startActivity(FeedBackActivity.class);
                    resideLayout.closePane();
                }
                break;
                // about author
                case 4: {
                    startActivity(AboutAuthorActivity.class);
                    resideLayout.closePane();
                }
                break;
                default:
                    break;
            }
        }));
    }

    private List<MainMenuBean> getMenuData()
    {
        menuBeans.clear();
        String[] menuName = getResources().getStringArray(R.array.main_menu_name);
        TypedArray menuIcon = getResources().obtainTypedArray(R.array.main_menu_icon);

        for (int i=0; i<menuName.length; ++i)
        {
            MainMenuBean menuBean = new MainMenuBean();
            menuBean.setName(menuName[i]);
            menuBean.setIcon(menuIcon.getResourceId(i, 0));
            menuBeans.add(menuBean);
        }

        return menuBeans;
    }

    private String getToolBarTitleString()
    {
        String[] menuName = getResources().getStringArray(R.array.main_menu_name);

        return menuName[currentFragment];
    }

    private String getFragmentTagById(int index)
    {
        String[] tag = getResources().getStringArray(R.array.main_menu_name);

        return tag[index];
    }

    private void switchFragment(int currentIndex)
    {
        if (currentIndex < CLASSIFY || CLASSIFY > SCANBOOK)
        {
            return;
        }

        if (getFragmentTagById(currentIndex).equals(currentFragmentTag))
        {
            return;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment fragment = fragmentManager.findFragmentByTag(currentFragmentTag);

        if (fragment != null)
        {
            fragmentTransaction.hide(fragment);
        }

        Fragment newFragment = fragmentManager.findFragmentByTag(getFragmentTagById(currentIndex));

        if (newFragment != null)
        {
            return;
        }

        switch (currentIndex)
        {
            case CLASSIFY:
                newFragment = BookClassifyFragment.newInstance();
                break;
            case BOOKSHELF:
                newFragment = BookShelfFragment.newInstance();
                break;
            case SCANBOOK:
                newFragment = ScanBookFragment.newInstance();
                break;
            default:
                break;
        }

        currentFragmentTag = getFragmentTagById(currentIndex);

        if (newFragment == null)
        {

        }
        else  if (newFragment.isAdded())
        {
            fragmentTransaction.show(newFragment);
        }
        else
        {
            fragmentTransaction.add(R.id.frame_container, newFragment, currentFragmentTag);
        }
        fragmentTransaction.commit();
    }

    @OnClick({R.id.avatar, R.id.tv_theme, R.id.tv_setting})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.avatar:
                String username = SharedPreUtils.getInstance().getString(USERNAME, "");
                if (username.isEmpty())
                {
                    startActivity(LoginActivity.class);
                }
                else
                {
                    startActivity(UserInfoActivity.class);
                }
                break;
            case R.id.tv_theme:
                new ColorChooserDialog.Builder(this, R.string.theme)
                        .customColors(R.array.colors, null)
                        .doneButton(R.string.done)
                        .cancelButton(R.string.cancel)
                        .allowUserColorInput(false)
                        .allowUserColorInputAlpha(false)
                        .show();
                break;
            case R.id.tv_setting:
                if (name.isEmpty())
                {
                    startActivity(LoginActivity.class);
                }
                else
                {
                    startActivity(SettingActivity.class);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
        if (selectedColor == ThemeUtils.getThemeColor2Array(this, R.attr.colorPrimary))
        {
            return;
        }

        if (selectedColor == getResources().getColor(R.color.colorBluePrimary))
        {
            setTheme(R.style.BlueTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Blue);
        }
        else if (selectedColor == getResources().getColor(R.color.colorRedPrimary))
        {
            setTheme(R.style.RedTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Red);
        }
        else if (selectedColor == getResources().getColor(R.color.colorBrownPrimary))
        {
            setTheme(R.style.BrownTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Brown);
        }
        else if (selectedColor == getResources().getColor(R.color.colorGreenPrimary))
        {
            setTheme(R.style.GreenTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Green);
        }
        else if (selectedColor == getResources().getColor(R.color.colorPurplePrimary))
        {
            setTheme(R.style.PurpleTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Purple);
        }
        else if (selectedColor == getResources().getColor(R.color.colorTealPrimary))
        {
            setTheme(R.style.TealTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Teal);
        }
        else if (selectedColor == getResources().getColor(R.color.colorPinkPrimary))
        {
            setTheme(R.style.PinkTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Pink);
        }
        else if (selectedColor == getResources().getColor(R.color.colorOrangePrimary))
        {
            setTheme(R.style.OrangeTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Orange);
        }
        else if (selectedColor == getResources().getColor(R.color.colorIndigoPrimary))
        {
            setTheme(R.style.IndigoTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Indigo);
        }
        else if (selectedColor == getResources().getColor(R.color.colorLightGreenPrimary))
        {
            setTheme(R.style.LightGreenTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.LightGreen);
        }
        else if (selectedColor == getResources().getColor(R.color.colorDeepOrangePrimary))
        {
            setTheme(R.style.DeepOrangeTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.DeepOrange);
        }
        else if (selectedColor == getResources().getColor(R.color.colorLimePrimary))
        {
            setTheme(R.style.LimeTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Lime);
        }
        else if (selectedColor == getResources().getColor(R.color.colorBlueGreyPrimary))
        {
            setTheme(R.style.BlueGreyTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.BlueGrey);
        }
        else if (selectedColor == getResources().getColor(R.color.colorCyanPrimary))
        {
            setTheme(R.style.CyanTheme);
            SharedPreUtils.getInstance().setCurrentTheme(Theme.Cyan);
        }

        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        if (null != localBitmap && rootView instanceof ViewGroup) {
            final View tmpView = new View(getApplicationContext());
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    ColorUiUtil.changeTheme(rootView, getTheme());
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        name = SharedPreUtils.getInstance().getString(USERNAME, "");

        try
        {
            if (!name.isEmpty())
            {
                UserBean userBean = UserHelper.getInstance().findUserByName(name);
                Glide.with(context).load(Constant.BASE_URL + userBean.getIcon())
                        .apply(new RequestOptions().transform(new CircleCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                        .into(avatar);
                tvDesc.setText("");
                tvSettings.setText(getString(R.string.setting));
            }
            else
            {
                Glide.with(context).load(R.mipmap.avatar)
                        .apply(new RequestOptions().transform(new CircleCrop()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                        .into(avatar);
                tvDesc.setText(getString(R.string.not_login));
                tvSettings.setText(getString(R.string.title_activity_login));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();        // must be deleted
        if (resideLayout.isOpen())
        {
            resideLayout.closePane();
        }
        else
        {
            long time = System.currentTimeMillis();

            if (time - firstTime < 2000)
            {
                finish();
            }
            else
            {
                SnackBarUtils.makeShort(getWindow().getDecorView(), getString(R.string.click_again_to_exist)).show();
                firstTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void appUpdate(AppUpdateBean appUpdateBean) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
