package com.hsf1002.sky.weread.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.db.entity.CollBookBean;
import com.hsf1002.sky.weread.db.helper.CollBookHelper;
import com.hsf1002.sky.weread.model.BookBean;
import com.hsf1002.sky.weread.utils.BaseUtils;
import com.hsf1002.sky.weread.utils.LoadingHelper;
import com.hsf1002.sky.weread.view.base.BaseActivity;
import com.hsf1002.sky.weread.viewmodel.activity.VMBookDetailInfo;
import com.hsf1002.sky.weread.widget.dialog.BookTagDialog;
import com.hsf1002.sky.weread.widget.theme.ColorRelativeLayout;
import com.hsf1002.sky.weread.widget.theme.ColorTextView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hsf1002.sky.weread.constant.Constant.BOOKID;

/**
 * Created by hefeng on 18-5-14.
 */

public class BookDetailActivity extends BaseActivity implements IBookDetail{

    @BindView(R.id.iv_book_image)
    ImageView bookImage;

    @BindView(R.id.tv_book_name)
    TextView bookTv;

    @BindView(R.id.ctv_book_author)
    ColorTextView authorCtv;

    @BindView(R.id.tv_book_classify)
    TextView classifyTv;

    @BindView(R.id.tv_word_updatetime)
    TextView updatetimeTv;

    @BindView(R.id.ctv_score)
    ColorTextView scoreCtv;

    @BindView(R.id.tv_fow_num)
    TextView fownumTv;

    @BindView(R.id.tv_good_num)
    TextView goodnumTv;

    @BindView(R.id.tv_word_count)
    TextView wordcountTv;

    @BindView(R.id.tv_book_brief)
    TextView bookbriefTv;

    @BindView(R.id.ll_fow)
    LinearLayout fowLL;

    @BindView(R.id.crl_start_read)
    ColorRelativeLayout startreadCrl;

    @BindView(R.id.ctv_addbook)
    TextView addbookTv;

    @BindView(R.id.tv_evaluate)
    TextView evaluateTv;

    @BindView(R.id.fl_tags)
    TagFlowLayout tagFl;

    @BindView(R.id.ll_tag)
    LinearLayout tagLl;

    @BindView(R.id.tv_copyright)
    TextView copyrightTv;

    @BindView(R.id.tv_read)
    TextView readTv;

    @BindView(R.id.rl_rootview)
    RelativeLayout rootviewRl;

    private CollBookBean collBookBean;
    private BookBean bookBean;
    private VMBookDetailInfo model;
    private String bookid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new VMBookDetailInfo(context, this);
        setBiddingView(R.layout.activity_book_detail, NO_BINDING, model);
        bookid = getIntent().getStringExtra(BOOKID);
        model.bookinfo(bookid);
    }

    //@Override
    private void init() {
        super.initView();

        initThemeToolBar(bookBean.getTitle());
        Glide.with(context).load(Constant.ZHUISHU_IMAGE_URL + bookBean.getCover())
                .into(bookImage);

        bookTv.setText(bookBean.getTitle());
        authorCtv.setText(bookBean.getAuthor());
        classifyTv.setText(" | " + bookBean.getMajorCate());
        wordcountTv.setText(bookBean.getSerializeWordCount() + "");
        fownumTv.setText(bookBean.getLatelyFollower() + "");
        goodnumTv.setText(bookBean.getRetentionRatio() + "%");
        bookbriefTv.setText(bookBean.getLongIntro());

        if (bookBean.getTags().size() > 0)
        {
            tagLl.setVisibility(View.VISIBLE);
            tagFl.setAdapter(new TagAdapter<String>(bookBean.getTags()) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.tags_tv, tagFl, false);
                    tv.setText(s);
                    return tv;
                }
            });

            tagFl.setOnTagClickListener((view, position, parent) ->
            {
                String tag = bookBean.getTags().get(position);
                showTagDialog(tag);
                return true;
            });
        }
        else
        {
            tagLl.setVisibility(View.GONE);
            tagFl.setVisibility(View.GONE);
        }

        String wordcount = bookBean.getWordCount() / 10000 > 0 ? bookBean.getWordCount() / 10000 + "wan" : bookBean.getWordCount() + "zi";

        if (bookBean.getRating() != null)
        {
            scoreCtv.setText(BaseUtils.format1Digits(bookBean.getRating().getScore()));
            evaluateTv.setText(bookBean.getRating().getCount() + "ping");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");

        try
        {
            Date d = format.parse(bookBean.getUpdated().replace("Z", " UTC"));//注意是空格+UTC
            Date nowDate = new Date();
            int day = (int) ((nowDate.getTime() - d.getTime()) / (1000 * 3600 * 24));
            int hour = (int) ((nowDate.getTime() - d.getTime()) / (1000 * 3600));
            String time = day > 0 ? day + "天前" : hour + "小时前";
            updatetimeTv.setText(wordcount + "  |  " + time);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        collBookBean = CollBookHelper.getInstance().findBookById(bookBean.get_id());

        if (bookBean.isCollect())
        {
            addbookTv.setText(getString(R.string.delete_from_shelf));
        }
        else
        {
            addbookTv.setText(getString(R.string.add_shelf));
        }

        if (collBookBean == null)
        {
            collBookBean = bookBean.getCollBookBean();
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
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rootviewRl, "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rootviewRl, "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(rootviewRl, "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = -display.getWidth() * 0.2f / 2;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(rootviewRl, "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(rootviewRl);
        animatorSet.start();

    }

    /**
     * 弹框关闭页面动画
     */
    protected void hideAnimator() {
        long duration = 500;
        float[] scale = new float[2];
        scale[0] = 0.8f;
        scale[1] = 1.0f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rootviewRl, "scaleX", scale).setDuration(duration);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rootviewRl, "scaleY", scale).setDuration(duration);
        float[] rotation = new float[]{0, 10, 0};
        ObjectAnimator rotationX = ObjectAnimator.ofFloat(rootviewRl, "rotationX", rotation).setDuration(duration);

        float[] translation = new float[1];
        translation[0] = 0;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(rootviewRl, "translationY", translation).setDuration(duration);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotationX, translationY);
        animatorSet.setTarget(rootviewRl);
        animatorSet.start();
    }

    @OnClick({R.id.ll_fow, R.id.crl_start_read})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_fow:
                String collectStatus = addbookTv.getText().toString();
                if (collectStatus.equals(getString(R.string.delete_from_shelf)))
                {
                    addbookTv.setText(getString(R.string.add_shelf));
                    model.deleteBookShelfToServer(collBookBean);
                }
                else
                {
                    addbookTv.setText(getString(R.string.delete_from_shelf));
                    model.addBookShelf(collBookBean);
                }
                break;
            case R.id.crl_start_read:
                Bundle bundle = new Bundle();
                bundle.putSerializable(ReadActivity.EXTRA_COLL_BOOK, collBookBean);
                bundle.putBoolean(ReadActivity.EXTRA_IS_COLLECTED, false);
                startActivity(ReadActivity.class, bundle);
                break;
            default:
                break;
        }
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
    public void getBookInfo(BookBean bookBean) {
        this.bookBean = bookBean;
        init();
    }
}
