package com.hsf1002.sky.weread.db.helper;

import com.hsf1002.sky.weread.db.entity.UserBean;
import com.hsf1002.sky.weread.db.gen.DaoSession;
import com.hsf1002.sky.weread.db.gen.UserBeanDao;

/**
 * Created by hefeng on 18-5-11.
 */

public class UserHelper {
    private static volatile  UserHelper sInstance;
    private static DaoSession daoSession;
    private static UserBeanDao userBeanDao;

    public static UserHelper getInstance()
    {
        if (sInstance == null)
        {
            synchronized (UserHelper.class)
            {
                if (sInstance == null)
                {
                    sInstance = new UserHelper();
                    daoSession = DaoDbHelper.getInstance().getDaoSession();
                    userBeanDao = daoSession.getUserBeanDao();
                }
            }
        }

        return  sInstance;
    }

    public void saveUser(UserBean userBean)
    {
        userBeanDao.insertOrReplace(userBean);
    }

    public void updateUser(UserBean userBean)
    {
        userBeanDao.update(userBean);
    }

    public void removeAllUser()
    {
        userBeanDao.deleteAll();
    }

    public UserBean findUserByName(String username)
    {
        return userBeanDao.queryBuilder().where(UserBeanDao.Properties.Name.eq(username)).unique() != null ?
                userBeanDao.queryBuilder().where(UserBeanDao.Properties.Name.eq(username)).unique() : null;
    }
}
