package com.hsf1002.sky.weread.db.helper;

import android.database.sqlite.SQLiteDatabase;

import com.hsf1002.sky.weread.application.WeReadApplication;
import com.hsf1002.sky.weread.db.gen.DaoMaster;
import com.hsf1002.sky.weread.db.gen.DaoSession;

/**
 * Created by hefeng on 18-5-11.
 */

public class DaoDbHelper {

    private static final String DB_NAME = "WeiYue";

    private static volatile DaoDbHelper sInstance;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public DaoDbHelper() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(WeReadApplication.getAppContext(), DB_NAME, null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoDbHelper getInstance()
    {
        if (sInstance == null)
        {
            synchronized (DaoDbHelper.class)
            {
                if (sInstance == null)
                {
                    sInstance = new DaoDbHelper();
                }
            }
        }

        return sInstance;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
