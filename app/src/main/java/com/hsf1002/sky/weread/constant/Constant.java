package com.hsf1002.sky.weread.constant;

import com.hsf1002.sky.weread.BuildConfig;
import com.hsf1002.sky.weread.utils.FileUtils;

import java.io.File;

/**
 * Created by hefeng on 18-5-9.
 */

public class Constant {
    public static String BASE_URL;

    static
    {
        if (!BuildConfig.DEBUG)
        {
            BASE_URL = "http://192.168.5.87:3389";
        }
        else
        {
            BASE_URL = "http://www.luliangdev.cn";
        }
    }
    public static final String ZHUISHU_IMAGE_URL = "http://statics.zhuishushenqi.com";
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";

    //BookCachePath (因为getCachePath引用了Context，所以必须是静态变量，不能够是静态常量)
    public static String BOOK_CACHE_PATH = FileUtils.getCachePath() + File.separator
            + "book_cache" + File.separator;


    public static final int SPLASH_ACTIVITY_DURATION = 1000;

    public static final String APP_THEME = "app_theme";
    public static final int TIP_TEXT_SIZE = 16;
    public static final int RELOAD_BUTTON_WIDTH = 160;
    public static final int RELOAD_BUTTON_HEIGHT = 80;

    public static final int CONNECT_TIMEOUT = 20;
    public static final int CONNECT_READ_TIMEOUT = 20;
    public static final int CONNECT_WRITE_TIMEOUT = 20;
    public static final String LOGGING_INTERCEPTOR_REQUEST = "Request";
    public static final String LOGGING_INTERCEPTOR_RESPONSE = "Response";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String TABNAME = "tabname";
    public static final String TABINDEX = "tabindex";
    public static final String TITLE_NAME = "titlename";
    public static final String GENDER = "gender";
    public static final String TYPE = "type";

    public static final String BOOKID = "bookid";
    public static final String BOOKIMAGE = "bookImage";

}
