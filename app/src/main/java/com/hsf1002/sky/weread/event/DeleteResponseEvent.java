package com.hsf1002.sky.weread.event;

import com.hsf1002.sky.weread.db.entity.CollBookBean;

/**
 * Created by hefeng on 18-5-16.
 */

public class DeleteResponseEvent {
    public boolean isDelete;
    public CollBookBean collBook;

    public DeleteResponseEvent(boolean isDelete, CollBookBean collBook) {
        this.isDelete = isDelete;
        this.collBook = collBook;
    }
}
