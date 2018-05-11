package com.hsf1002.sky.weread.model;

import java.io.Serializable;

/**
 * Created by hefeng on 18-5-10.
 */

public class MainMenuBean implements Serializable {
    private String name;
    private int icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
