package com.hsf1002.sky.weread.model;

/**
 * Created by hefeng on 18-5-10.
 */

public class AppUpdateBean {
    private int versionCode;
    private String downloadUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
