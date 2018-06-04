 RxHttpUtils + RxPermissions + Glide + SmartRefreshLayout + BaseRecyclerViewAdapterHelper + Material-dialogs + NavigationTabStrip + ExpandTextView + FlowLayout + greenDAO + NumberProgressBar + TakePhoto + GanK + Android-SpinKit


## RxPermissions
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

## ColorChooserDialog
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

## MaterialDialog
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
## NavigationTabStrip
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
