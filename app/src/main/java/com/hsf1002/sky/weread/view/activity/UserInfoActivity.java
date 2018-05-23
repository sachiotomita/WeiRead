package com.hsf1002.sky.weread.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.db.entity.UserBean;
import com.hsf1002.sky.weread.db.helper.UserHelper;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.utils.LoadingHelper;
import com.hsf1002.sky.weread.utils.SharedPreUtils;
import com.hsf1002.sky.weread.utils.ThemeUtils;
import com.hsf1002.sky.weread.utils.ToastUtils;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMUserInfo;
import com.hsf1002.sky.weread.widget.dialog.BookTagDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.hsf1002.sky.weread.constant.Constant.BASE_URL;
import static com.hsf1002.sky.weread.constant.Constant.BOOKID;
import static com.hsf1002.sky.weread.constant.Constant.USERNAME;

/**
 * Created by hefeng on 18-5-11.
 */

public class UserInfoActivity extends BaseActivity implements IUserInfo, TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.iv_avatar)
    ImageView avatarIv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.tv_name)
    TextView nameTv;

    @BindView(R.id.et_nickname)
    EditText nicknameEt;

    @BindView(R.id.et_brief)
    EditText briefEt;

    @BindView(R.id.ll_tip)
    LinearLayout tipLl;

    @BindView(R.id.tv_nickname)
    TextView nicknameTv;

    @BindView(R.id.tv_brief)
    TextView briefTv;

    @BindView(R.id.tv_books)
    TextView booksTv;

    @BindView(R.id.fl_book_name)
    TagFlowLayout booknameTagFlow;

    @BindView(R.id.tv_book_tags)
    TextView booktagsTv;

    @BindView(R.id.fl_book_type)
    TagFlowLayout booktypeTagFlow;

    @BindView(R.id.cv_like)
    CardView likeCv;

    @BindView(R.id.fab_edit_password)
    FloatingActionButton passwordFab;

    @BindView(R.id.fab_edit_userinfo)
    FloatingActionButton userinfoFab;

    @BindView(R.id.fab_menu)
    FloatingActionsMenu menuFab;


    @BindView(R.id.btn_confirm)
    Button confirmBtn;

    @BindView(R.id.cl_root)
    CoordinatorLayout coordinatorLayout;

    private TakePhoto takePhoto;
    private CropOptions cropOptions;
    private CompressConfig compressConfig;
    private Uri imageUri;
    private InvokeParam invokeParam;
    private VMUserInfo model;
    private String passwordStr;
    private String usernameStr;
    private UserBean userBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new VMUserInfo(context, this);
        setBiddingView(R.layout.activity_user_info, NO_BINDING, model);
    }

    @Override
    protected void initView() {
        super.initView();

        stopEdit();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbarLayout.setTitle(getString(R.string.title_activity_user));

        usernameStr = SharedPreUtils.getInstance().getString(USERNAME, "");
        userBean = UserHelper.getInstance().findUserByName(usernameStr);

        model.getUserInfo();
        briefEt.setText(userBean.getBrief());
        nameTv.setText(usernameStr);
        nicknameEt.setText(userBean.getNickname());
        Glide.with(context).load(BASE_URL + userBean.getIcon())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15)).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
                .into(avatarIv);

        takePhoto = getTakePhoto();
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(compressConfig, false);
    }

    public TakePhoto getTakePhoto()
    {
        if (takePhoto == null)
        {
            takePhoto = (TakePhoto)TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }

        return takePhoto;
    }

    @Override
    public void showLoading() {
        LoadingHelper.getInstance().showLoading(context);
    }

    @Override
    public void stopLoading() {
        LoadingHelper.getInstance().hideLoading();
    }

    @Override
    public void uploadSuccess(String imageUrl) {
        ToastUtils.show(R.string.change_avatar_success);

        Glide.with(context).load(BASE_URL + imageUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15)).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(avatarIv);
    }

    @Override
    public void userInfo(UserBean userBean) {
        List<String> likeBooks = new ArrayList<>();
        List<String > bookTags = new ArrayList<>();
        List<BookBean> bookBeanList = userBean.getLikebooks();

        if (bookBeanList.size() > 0)
        {
            likeCv.setVisibility(View.VISIBLE);

            for (BookBean bookBean : bookBeanList)
            {
                likeBooks.add(bookBean.getTitle());
                bookTags.addAll(bookBean.getTags());
            }

            booknameTagFlow.setAdapter(new TagAdapter<String>(likeBooks) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tags_tv, booknameTagFlow, false);
                    tv.setText(s);
                    return tv;
                }
            });

            booknameTagFlow.setOnTagClickListener((view, position, parent) ->
            {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BOOKID, bookBeanList.get(position).get_id());
                startActivity(intent);
                return true;
            });

            if (bookTags.size() > 0)
            {
                booktagsTv.setVisibility(View.VISIBLE);
                booktypeTagFlow.setVisibility(View.VISIBLE);
                booktypeTagFlow.setAdapter(new TagAdapter<String>(bookTags)
                {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView)LayoutInflater.from(context).inflate(R.layout.tags_tv, booktypeTagFlow, false);
                        tv.setText(s);
                        return tv;
                    }
                });
                booktypeTagFlow.setOnTagClickListener((view, position, parent) ->
                {
                    String tag = bookTags.get(position);
                    showTagDialog(tag);
                    return true;
                });
            }
            else
            {
                booktagsTv.setVisibility(View.GONE);
                booktypeTagFlow.setVisibility(View.GONE);
            }
        }
        else
        {
            likeCv.setVisibility(View.GONE);
        }
    }

    private void showTagDialog(String tag)
    {
        BookTagDialog bookTagDialog = new BookTagDialog(context, tag);
        bookTagDialog.show();
        bookTagDialog.setOnDismissListener(dialog -> hideAnimator());


        long duration = 500;
        Display display = getWindowManager().getDefaultDisplay();
        float[] scale = new float[2];
        scale[0] = 1.0f;
        scale[1] = 0.8f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(coordinatorLayout
                , "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(coordinatorLayout
                , "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(coordinatorLayout
                , "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = -display.getWidth() * 0.2f / 2;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(coordinatorLayout
                , "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(coordinatorLayout
        );
        animatorSet.start();
    }


    protected void hideAnimator() {
        long duration = 500;
        float[] scale = new float[2];
        scale[0] = 0.8f;
        scale[1] = 1.0f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(coordinatorLayout
                , "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(coordinatorLayout
                , "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(coordinatorLayout
                , "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = 0;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(coordinatorLayout
                , "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(coordinatorLayout
        );
        animatorSet.start();
    }

    @Override
    public void takeSuccess(TResult result) {
        String path = result.getImage().getOriginalPath();
        model.uploadAvatar(path);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());

        if (PermissionManager.TPermissionType.WAIT.equals(type))
        {
            this.invokeParam = invokeParam;
        }

        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startEdit()
    {
        nicknameEt.setFocusable(true);
        nicknameEt.setFocusableInTouchMode(true);
        briefEt.setFocusable(true);
        briefEt.setFocusableInTouchMode(true);
        nameTv.setBackgroundColor(getResources().getColor(R.color.color_ccc));
        tipLl.setVisibility(View.VISIBLE);
        confirmBtn.setVisibility(View.VISIBLE);
    }

    private void stopEdit()
    {
        nicknameEt.setFocusable(false);
        nicknameEt.setFocusableInTouchMode(false);
        briefEt.setFocusable(false);
        briefEt.setFocusableInTouchMode(false);
        nameTv.setBackgroundColor(getResources().getColor(R.color.transparent));
        tipLl.setVisibility(View.GONE);
        confirmBtn.setVisibility(View.GONE);
    }

    @OnClick({R.id.fab_edit_userinfo, R.id.fab_edit_password, R.id.iv_avatar, R.id.btn_confirm})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.fab_edit_userinfo:
                menuFab.toggle();
                startEdit();
                break;
            case R.id.fab_edit_password:
                menuFab.toggle();
                new MaterialDialog.Builder(this)
                        .title(getString(R.string.modify_password))
                        .inputRange(2, 20, ThemeUtils.getThemeColor())
                        .input(getString(R.string.input_password), null, (dialog, input) ->
                        {
                            dialog.dismiss();
                            model.updatePassword(input.toString());
                        }).show();
                break;
            case R.id.iv_avatar:
                final String[] items = {"gallery", "capture"};
                new MaterialDialog.Builder(this)
                        .title(getString(R.string.select_capture_way))
                        .items(items)
                        .itemsCallback((dialog, itemView, position, text) ->
                        {
                            switch (position)
                            {
                                case 0:
                                    dialog.dismiss();
                                    imageUri = getImageCropUri();
                                    takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                                    break;
                                case 1:
                                    dialog.dismiss();
                                    imageUri = getImageCropUri();
                                    takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                                    break;
                                default:
                                    break;
                            }
                        })
                        .show();
                break;
            case R.id.btn_confirm:
                new MaterialDialog.Builder(this)
                        .title(getString(R.string.modify_username))
                        .content(getString(R.string.modify_verify))
                        .negativeText(getString(R.string.cancel))
                        .onNegative((dialog, which) -> dialog.dismiss())
                        .positiveText(getString(R.string.sure))
                        .onPositive((dialog, which) ->
                        {
                           String nickname = nicknameEt.getText().toString();
                           String brief = briefEt.getText().toString();

                           if (TextUtils.isEmpty(nickname)) {
                               ToastUtils.show(R.string.nickname_cannot_empty);
                               return;
                           }

                           if (TextUtils.isEmpty(brief))
                           {
                               ToastUtils.show(R.string.my_pledge_cannot_empty);
                               return;
                           }

                           stopEdit();
                           dialog.dismiss();
                           model.updateUserInfo(nickname, brief);
                        }).show();
                break;
            default:
                break;
        }
    }

    private Uri getImageCropUri()
    {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdir();
        }

        return Uri.fromFile(file);
    }


}
