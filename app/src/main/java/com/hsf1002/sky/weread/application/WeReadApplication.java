package com.hsf1002.sky.weread.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.allen.library.RxHttpUtils;
import com.hsf1002.sky.weread.R;
import com.hsf1002.sky.weread.constant.Constant;
import com.hsf1002.sky.weread.utils.ThemeUtils;
import com.hsf1002.sky.weread.view.service.BookDownloadService;
import com.hsf1002.sky.weread.widget.CircleHeader;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.weavey.loading.lib.LoadingLayout;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;


import static com.hsf1002.sky.weread.constant.Constant.CONNECT_READ_TIMEOUT;
import static com.hsf1002.sky.weread.constant.Constant.CONNECT_TIMEOUT;
import static com.hsf1002.sky.weread.constant.Constant.CONNECT_WRITE_TIMEOUT;
import static com.hsf1002.sky.weread.constant.Constant.LOGGING_INTERCEPTOR_REQUEST;
import static com.hsf1002.sky.weread.constant.Constant.LOGGING_INTERCEPTOR_RESPONSE;
import static com.hsf1002.sky.weread.constant.Constant.RELOAD_BUTTON_HEIGHT;
import static com.hsf1002.sky.weread.constant.Constant.RELOAD_BUTTON_WIDTH;
import static com.hsf1002.sky.weread.constant.Constant.TIP_TEXT_SIZE;

/**
 * Created by hefeng on 18-5-9.
 */

public class WeReadApplication extends Application {

    private static WeReadApplication app;
    public static PackageInfo packageInfo;

    public static Context getAppContext()
    {
        return app;
    }

    public static Resources getAppResources()
    {
        return app.getResources();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init()
    {
        app = this;

        try {
            packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        startService(new Intent(getAppContext(), BookDownloadService.class));
        initRxHttpUtils();
        initRefresh();
        initLoadingLayout();
    }

    private void initLoadingLayout()
    {
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
    }

    private void initRxHttpUtils()
    {
        RxHttpUtils.init(this);
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(CONNECT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor.Builder()
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request(LOGGING_INTERCEPTOR_REQUEST)
                    .response(LOGGING_INTERCEPTOR_RESPONSE)
                    .build());

        RxHttpUtils.getInstance()
                .config()
                .setBaseUrl(Constant.BASE_URL)
                .setCookie(false)
                .setOkClient(client.build())
                .setLog(true);
    }

    private void initRefresh()
    {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) ->
        {
            CircleHeader header = new CircleHeader(context);
            layout.setPrimaryColorsId(ThemeUtils.getThemeColorId(), R.color.white);
            return header;
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
        //SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Translate));
    }
}
