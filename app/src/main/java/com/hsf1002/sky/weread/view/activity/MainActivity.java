package com.hsf1002.sky.weread.view.activity;

import android.Manifest;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.model.AppUpdateBean;
import com.hsf1002.sky.weread.model.MainMenuBean;
import com.hsf1002.sky.weread.utils.BaseUtils;
import com.hsf1002.sky.weread.utils.SnackBarUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.adapter.MainMenuAdapter;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.widget.ResideLayout;
import com.hsf1002.sky.weread.widget.theme.ColorRelativeLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


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
    private static final int FEEDBACK = 3;
    private static final int ABOUTAUTHOR = 4;
    private static int currentFragment = CLASSIFY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new VMSettingInfo(this, this);
        setBiddingView(R.layout.activity_main, NO_BINDING, model);
        initThemeToolBar("classify", R.drawable.ic_classify, R.drawable.ic_search, v ->
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

    public void switchFragment(int fragment)
    {
        switch (fragment)
        {
            case CLASSIFY:

                break;
            case BOOKSHELF:

                break;
            case SCANBOOK:

                break;
            default:
                break;
        }
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {

    }

    @Override
    public void appUpdate(AppUpdateBean appUpdateBean) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
