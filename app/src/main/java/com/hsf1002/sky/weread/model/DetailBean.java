package com.hsf1002.sky.weread.model;

import java.util.List;

/**
 * Created by hefeng on 18-5-16.
 */

public class DetailBean<T> {

    private T detail;
    private List<CommentBean> bestComments;
    private List<CommentBean> comments;

    public DetailBean(T detail, List<CommentBean> bestComments, List<CommentBean> comments) {
        this.detail = detail;
        this.bestComments = bestComments;
        this.comments = comments;
    }

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }

    public List<CommentBean> getBestComments() {
        return bestComments;
    }

    public void setBestComments(List<CommentBean> bestComments) {
        this.bestComments = bestComments;
    }

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }
}
