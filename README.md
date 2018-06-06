 RxHttpUtils + RxPermissions + Glide + SmartRefreshLayout + BaseRecyclerViewAdapterHelper + Material-dialogs + NavigationTabStrip + ExpandTextView + FlowLayout + greenDAO + NumberProgressBar + TakePhoto + GanK + Android-SpinKit


## RxPermissions 6.0权限库
```
implementation "com.tbruyelle.rxpermissions2:rxpermissions:0.9.4@aar"
```
```
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
```

## ColorChooserDialog MD风格选择Dialog
```
implementation "com.afollestad.material-dialogs:core:0.9.0.2"
implementation "com.afollestad.material-dialogs:commons:0.9.0.2"
```
```
@OnClick({R.id.avatar, R.id.tv_theme, R.id.tv_setting})
public void onClick(View view)
{
    switch (view.getId())
    {
      case R.id.tv_theme:
            new ColorChooserDialog.Builder(this, R.string.theme)
            .customColors(R.array.colors, null)
            .doneButton(R.string.done)
            .cancelButton(R.string.cancel)
            .allowUserColorInput(false)
            .allowUserColorInputAlpha(false)
            .show();
    break;
    ...
}
```
```
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
       ...
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
```

## MaterialDialog MD风格Dialog
```
implementation "com.afollestad.material-dialogs:core:0.9.0.2"
implementation "com.afollestad.material-dialogs:commons:0.9.0.2"
```
```
new MaterialDialog.Builder(context)
        .title(getResources().getString(R.string.tip))
        .content("file not existed, delete?")
        .positiveText(getResources().getString(R.string.sure))
        .onPositive((dialog, which) -> deleteBook(collBookBean, position))
        .negativeText(getResources().getString(R.string.cancel))
        .onNegative((dialog, which) -> dialog.dismiss()).show();
```
```

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
                downloadBook(collBook);
                break;
            case "delete":
                deleteBook(collBook, position);
                break;
            default:
                break;
        }
    }
```
```
new MaterialDialog.Builder(context)
            .title("delete local book")
            .checkBoxPrompt("delete local file at the same time", false, (buttonView, isChecked) -> isCheck = isChecked)
            .positiveText(R.string.sure)
            .onPositive((dialog, which) ->
            {
                if (isCheck)
                {
                    ...
                }
                else
                {
                    ...
                }
                adapter.notifyDataSetChanged();
            })
            .negativeText(R.string.cancel)
            .onNegative((dialog, which) ->
            {
                dialog.dismiss();
            })
            .show();
```
## LoadingLayout 进入动画加载
```
implementation "com.lai.weavey:loadinglayout:1.3.1"
```
```
<?xml version="1.0" encoding="utf-8"?>
<com.weavey.loading.lib.LoadingLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/loadinglayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    app:isFirstVisible="true">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/rv_classify"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

</com.weavey.loading.lib.LoadingLayout>
```
```
// 在Application中初始化  
LoadingLayout.getConfig()
        .setErrorText(getString(R.string.error_try_again))
        .setEmptyText(getString(R.string.sorry_no_data))
        .setNoNetworkText(getString(R.string.no_network_check))
        .setErrorImage(R.drawable.ic_error_icon)
        .setEmptyImage(R.drawable.ic_empty_error)
        .setNoNetworkImage(R.drawable.ic_net_error)
        .setAllTipTextColor(R.color.black)
        .setAllTipTextSize(TIP_TEXT_SIZE)
        .setReloadButtonText(getString(R.string.click_retry))
        .setReloadButtonTextSize(TIP_TEXT_SIZE)
        .setReloadButtonTextColor(R.color.black)
        .setReloadButtonWidthAndHeight(RELOAD_BUTTON_WIDTH, RELOAD_BUTTON_HEIGHT);
```
```
@BindView(R.id.loadinglayout)
LoadingLayout loadingLayout;
...
@Override
public void emptyData() {
    loadingLayout.setStatus(LoadingLayout.Empty);
}

@Override
public void errorData(String error) {
    loadingLayout.setEmptyText(error).setStatus(LoadingLayout.Error);
}

@Override
public void networkError() {
    loadingLayout.setStatus(LoadingLayout.No_Network);
}

@Override
public void getBookClassify(BookClassifyBean bookClassifyBean) {
    loadingLayout.setStatus(LoadingLayout.Success);
...
}
```
## NavigationTabStrip 横向Tab选择
```
    implementation "com.github.devlight.navigationtabstrip:navigationtabstrip:1.0.4"
```
```
<com.gigamole.navigationtabstrip.NavigationTabStrip
                android:id="@+id/nts_classify"
                android:layout_width="match_parent"
                android:layout_height="40dp"
                app:nts_active_color="@color/white"
                app:nts_animation_duration="300"
                app:nts_color="@color/white"
                app:nts_corners_radius="1.5dp"
                app:nts_factor="2.5"
                app:nts_gravity="bottom"
                app:nts_inactive_color="#c4c4c4"
                app:nts_size="15sp"
                app:nts_type="line"
                app:nts_typeface="fonts/typeface.otf"
                app:nts_weight="3dp"/>
<android.support.v4.view.ViewPager
                android:id="@+id/vp_classify"
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1"
                />                
```
```
@BindView(R.id.nts_classify)
NavigationTabStrip navigationTabStrip;

@Override
public void initView() {
    for (int i=0; i< titles.length; ++i)
    {
        fragments.add(ClassifyFragment.newInstance(titles[i], navigationTabStrip.getTabIndex()));
    }
    ...
    navigationTabStrip.setTitles(titles);
    navigationTabStrip.setViewPager(viewPager);
}
```

## ExpandTextView 展开折叠TextView
```
implementation "com.lcodecorex:extextview:1.0.2"
```
```
<com.lcodecore.extextview.ExpandTextView
        android:id="@+id/tv_book_brief"
        style="@style/book_detail_51.12"
        android:layout_width="match_parent"
        android:layout_marginBottom="10dp"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="10dp"
        android:lineSpacingExtra="4dp"
        android:text="发挥第三方空间都是开发商佛山地块附近的飞机螺丝钉机发牢骚附近立法监督法律解释法律监督了"
        app:arrowAlign="right"
        app:arrowPadding="8dp"
        app:arrowPosition="below"
        app:maxCollapsedLines="3"
        />
```
```
@BindView(R.id.tv_book_brief)
TextView bookbriefTv;
...
bookbriefTv.setText(bookBean.getLongIntro());
```

## SpinKitView 数据加载动画
```
implementation "com.github.ybq:Android-SpinKit:1.1.0"
```
```
<?xml version="1.0" encoding="utf-8"?>
<com.hsf1002.sky.weread.widget.theme.ColorRelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:background="@drawable/shape_rect_white"
    android:id="@+id/crl_loading"
    android:orientation="vertical"
    android:padding="20dp">

    <com.github.ybq.android.spinkit.SpinKitView
        android:id="@+id/spin_kit"
        style="@style/SpinKitView.CubeGrid"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        app:SpinKit_Color="@color/white"
        />

</com.hsf1002.sky.weread.widget.theme.ColorRelativeLayout>
```
```
public class LoadingHelper {
    private static Dialog dialog = null;
    private static LoadingHelper loadingHelper = null;
    private SpinKitView mSpinKitView;
    Context context;

    public static LoadingHelper getInstance() {
        if (loadingHelper == null) {
            loadingHelper = new LoadingHelper();
        }
        return loadingHelper;
    }

    public void showLoading(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mSpinKitView = dialog.findViewById(R.id.spin_kit);
        mSpinKitView.setColor(ThemeUtils.getThemeColor());
        dialog.show();
    }

    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
```
```
@Override
public void showLoading() {
    LoadingHelper.getInstance().showLoading(context);
}

@Override
public void stopLoading() {
    LoadingHelper.getInstance().hideLoading();
}
```

## TakePhoto 照片选择器
```
implementation "com.jph.takephoto:takephoto_library:4.0.3"
```
```
private TakePhoto takePhoto;
private CropOptions cropOptions;
private CompressConfig compressConfig;
private Uri imageUri;
private InvokeParam invokeParam;
...
takePhoto = getTakePhoto();
cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
takePhoto.onEnableCompress(compressConfig, false);
```
```
public TakePhoto getTakePhoto()
{
    if (takePhoto == null)
    {
        takePhoto = (TakePhoto)TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
    }

    return takePhoto;
}
```
```
@Override
protected void onSaveInstanceState(Bundle outState) {
    getTakePhoto().onSaveInstanceState(outState);
    super.onSaveInstanceState(outState);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    getTakePhoto().onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
}
```
```
private Uri getImageCropUri()
{
    File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
    if (!file.getParentFile().exists())
    {
        file.getParentFile().mkdir();
    }

    return Uri.fromFile(file);
}
...
switch (position)
{
    // pick from gallery
    case 0:
        dialog.dismiss();
        imageUri = getImageCropUri();
        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
        break;
    // capturing
    case 1:
        dialog.dismiss();
        imageUri = getImageCropUri();
        takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
        break;
    default:
        break;
}
```
