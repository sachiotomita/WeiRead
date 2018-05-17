package com.hsf1002.sky.weread.utils;

import android.os.Environment;

import com.hsf1002.sky.weread.application.WeReadApplication;

import java.io.File;

/**
 * Created by hefeng on 18-5-16.
 */

public class FileUtils {

    //判断是否挂载了SD卡
    public static boolean isSdCardExist(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;
    }

    //获取Cache文件夹
    public static String getCachePath(){
        if (isSdCardExist()){
            return WeReadApplication.getAppContext()
                    .getExternalCacheDir()
                    .getAbsolutePath();
        }
        else{
            return WeReadApplication.getAppContext()
                    .getCacheDir()
                    .getAbsolutePath();
        }
    }

    //递归删除文件夹下的数据
    public static synchronized void deleteFile(String filePath){
        File file = new File(filePath);
        if (!file.exists())
        {
            return;
        }

        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File subFile : files){
                String path = subFile.getPath();
                deleteFile(path);
            }
        }
        //删除文件
        file.delete();
    }
}
