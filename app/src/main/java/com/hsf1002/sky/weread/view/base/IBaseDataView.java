package com.hsf1002.sky.weread.view.base;

/**
 * Created by hefeng on 18-5-14.
 */

public interface IBaseDataView extends IBaseLoadView {
    void emptyData();
    void errorData(String error);
    void networkError();
}
