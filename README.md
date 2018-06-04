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
