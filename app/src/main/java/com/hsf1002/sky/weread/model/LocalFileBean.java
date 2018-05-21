package com.hsf1002.sky.weread.model;

import java.io.File;
import java.io.Serializable;

/**
 * Created by hefeng on 18-5-21.
 */

public class LocalFileBean implements Serializable {
    private File file;
    private boolean isSelect;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
