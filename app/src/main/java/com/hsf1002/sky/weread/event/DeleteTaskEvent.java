package com.hsf1002.sky.weread.event;

import com.hsf1002.sky.weread.db.entity.CollBookBean;

/**
 * Created by hefeng on 18-5-16.
 */

public class DeleteTaskEvent {
    public CollBookBean collBook;

    public DeleteTaskEvent(CollBookBean collBook) {
        this.collBook = collBook;
    }
}
